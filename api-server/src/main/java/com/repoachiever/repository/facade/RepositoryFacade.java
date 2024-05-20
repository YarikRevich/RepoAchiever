package com.repoachiever.repository.facade;

import com.repoachiever.dto.RepositoryContentUnitDto;
import com.repoachiever.entity.repository.ContentEntity;
import com.repoachiever.entity.repository.ExporterEntity;
import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.entity.repository.SecretEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.*;
import com.repoachiever.repository.*;
import com.repoachiever.repository.common.RepositoryConfigurationHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Represents facade for repository implementations used to handle tables.
 */
@ApplicationScoped
public class RepositoryFacade {
    @Inject
    ConfigRepository configRepository;

    @Inject
    ContentRepository contentRepository;

    @Inject
    ProviderRepository providerRepository;

    @Inject
    ExporterRepository exporterRepository;

    @Inject
    SecretRepository secretRepository;

    /**
     * Retrieves content state for the given configuration properties.
     *
     * @param contentStateApplication given content state retrieve application.
     * @return retrieve content state hash.
     */
    public String retrieveContentState(ContentStateApplication contentStateApplication) {
        return "";
    }

    /**
     * Retrieves all the available locations for the given configuration properties.
     *
     * @param contentRetrievalApplication given content retrieval application.
     * @return retrieved locations for the given configuration properties.
     */
    public List<String> retrieveLocations(ContentRetrievalApplication contentRetrievalApplication) {
        return null;
    }

    /**
     * Checks if content location is present in content repository with the help of the given content download
     * application.
     *
     * @param contentDownload given content download application.
     * @return result of the check.
     * @throws ContentValidationFailureException if content validation fails.
     */
    public Boolean isContentLocationValid(ContentDownload contentDownload) throws ContentValidationFailureException {
        ProviderEntity provider;

        try {
            provider = providerRepository.findByName(contentDownload.getProvider().toString());
        } catch (RepositoryOperationFailureException e) {
            throw new ContentValidationFailureException(e.getMessage());
        }

        Optional<String> credentials = RepositoryConfigurationHelper.getExternalCredentials(
                contentDownload.getProvider(), contentDownload.getCredentials().getExternal());

        try {
            if (!secretRepository.isPresentBySessionAndCredentials(
                    contentDownload.getCredentials().getInternal().getId(), credentials)) {
                return false;
            }
        } catch (RepositoryOperationFailureException e) {
            throw new ContentValidationFailureException(e.getMessage());
        }

        SecretEntity secret;

        try {
            secret = secretRepository.findBySessionAndCredentials(
                    contentDownload.getCredentials().getInternal().getId(),
                    credentials);
        } catch (RepositoryOperationFailureException e) {
            throw new ContentValidationFailureException(e.getMessage());
        }

        try {
            return contentRepository
                    .findByProviderAndSecret(provider.getId(), secret.getId())
                    .stream()
                    .anyMatch(element -> Objects.equals(element.getLocation(), contentDownload.getLocation()));
        } catch (RepositoryOperationFailureException e) {
            throw new ContentValidationFailureException(e);
        }
    }

    /**
     * Retrieves all the data from content repository in a form of content applications.
     *
     * @return retrieved set of content applications.
     * @throws ContentApplicationRetrievalFailureException if content application retrieval fails.
     */
    public List<ContentApplication> retrieveContentApplication() throws ContentApplicationRetrievalFailureException {
        List<ContentApplication> result = new ArrayList<>();

        List<RepositoryContentUnitDto> units = new ArrayList<>();

        List<ContentEntity> contents;

        try {
            contents = contentRepository.findAll();
        } catch (RepositoryOperationFailureException e) {
            throw new ContentApplicationRetrievalFailureException(e.getMessage());
        }

        for (ContentEntity content : contents) {
            ProviderEntity rawProvider;

            try {
                rawProvider = providerRepository.findById(content.getProvider());
            } catch (RepositoryOperationFailureException e) {
                throw new ContentApplicationRetrievalFailureException(e.getMessage());
            }

            Provider provider =
                    RepositoryConfigurationHelper.convertRawProviderToContentProvider(
                            rawProvider.getName());

            SecretEntity rawSecret;

            try {
                rawSecret = secretRepository.findById(content.getSecret());
            } catch (RepositoryOperationFailureException e) {
                throw new ContentApplicationRetrievalFailureException(e.getMessage());
            }

            CredentialsFieldsFull credentials =
                    RepositoryConfigurationHelper.convertRawSecretsToContentCredentials(
                            provider, rawSecret.getSession(), rawSecret.getCredentials());

            Optional<Exporter> exporter;

            if (content.getExporter().isPresent()) {
                ExporterEntity rawExporter;

                try {
                    rawExporter = exporterRepository.findById(content.getExporter().get());
                } catch (RepositoryOperationFailureException e) {
                    throw new RuntimeException(e);
                }

                exporter = Optional.of(
                        RepositoryConfigurationHelper.convertRawExporterToContentExporter(
                                rawExporter.getHost()));
            } else {
                exporter = Optional.empty();
            }

            units.add(RepositoryContentUnitDto.of(
                    content.getLocation(),
                    content.getAdditional(),
                    provider,
                    exporter,
                    credentials));
        }

        Map<CredentialsFieldsFull, Map<Provider, Map<Optional<Exporter>, List<RepositoryContentUnitDto>>>> groups =
                units
                        .stream()
                        .collect(
                                groupingBy(
                                        RepositoryContentUnitDto::getCredentials,
                                        groupingBy(RepositoryContentUnitDto::getProvider,
                                                groupingBy(RepositoryContentUnitDto::getExporter))));

        groups
                .forEach((key1, value1) -> {
                    value1
                            .forEach((key2, value2) -> {
                                value2
                                        .forEach((key3, value3) -> {
                                            result.add(
                                                    ContentApplication.of(
                                                            ContentUnit.of(
                                                                    value3
                                                                            .stream()
                                                                            .map(element ->
                                                                                    LocationsUnit.of(
                                                                                            element.getLocation(),
                                                                                            element.getAdditional()))
                                                                            .toList()),
                                                            key2,
                                                            key3.orElse(null),
                                                            key1));
                                        });
                            });
                });

        return result;
    }

    /**
     * Applies given content application, updating previous state.
     *
     * @param contentApplication given content application used for topology configuration.
     * @throws RepositoryContentApplicationFailureException if RepoAchiever Cluster repository content application failed.
     */
    public void apply(ContentApplication contentApplication) throws RepositoryContentApplicationFailureException {
        ProviderEntity provider;

        try {
            provider = providerRepository.findByName(contentApplication.getProvider().toString());
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentApplicationFailureException(e.getMessage());
        }

        Optional<String> credentials = RepositoryConfigurationHelper.getExternalCredentials(
                contentApplication.getProvider(), contentApplication.getCredentials().getExternal());

        try {
            if (!secretRepository.isPresentBySessionAndCredentials(
                    contentApplication.getCredentials().getInternal().getId(), credentials)) {
                secretRepository.insert(
                        contentApplication.getCredentials().getInternal().getId(),
                        credentials);
            }
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentApplicationFailureException(e.getMessage());
        }

        SecretEntity secret;

        try {
            secret = secretRepository.findBySessionAndCredentials(
                    contentApplication.getCredentials().getInternal().getId(),
                    credentials);
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentApplicationFailureException(e.getMessage());
        }

        Optional<Integer> exporter;

        Optional<Exporter> exporterField = RepositoryConfigurationHelper.getExporter(
                contentApplication.getProvider(), contentApplication.getExporter());

        if (exporterField.isPresent()) {
            try {
                if (!exporterRepository.isPresentByHost(exporterField.get().getHost())) {
                    exporterRepository.insert(exporterField.get().getHost());
                }
            } catch (RepositoryOperationFailureException e) {
                throw new RepositoryContentApplicationFailureException(e.getMessage());
            }

            ExporterEntity rawExporter;

            try {
                rawExporter = exporterRepository.findByHost(exporterField.get().getHost());
            } catch (RepositoryOperationFailureException e) {
                throw new RepositoryContentApplicationFailureException(e.getMessage());
            }

            exporter = Optional.of(rawExporter.getId());
        } else {
            exporter = Optional.empty();
        }

        try {
            contentRepository.deleteBySecret(secret.getId());
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentApplicationFailureException(e.getMessage());
        }

        for (LocationsUnit location : contentApplication.getContent().getLocations()) {
            try {
                contentRepository.insert(
                        location.getName(), location.getAdditional(), provider.getId(), exporter, secret.getId());
            } catch (RepositoryOperationFailureException e) {
                throw new RepositoryContentApplicationFailureException(e.getMessage());
            }
        }
    }

    /**
     * Applies given content withdrawal, removing previous state.
     *
     * @param contentWithdrawal given content application used for topology configuration.
     * @throws RepositoryContentDestructionFailureException if RepoAchiever Cluster repository content destruction failed.
     */
    public void destroy(ContentWithdrawal contentWithdrawal) throws RepositoryContentDestructionFailureException {
        Optional<String> credentials = RepositoryConfigurationHelper.getExternalCredentials(
                contentWithdrawal.getProvider(), contentWithdrawal.getCredentials().getExternal());

        SecretEntity secret;

        try {
            secret = secretRepository.findBySessionAndCredentials(
                    contentWithdrawal.getCredentials().getInternal().getId(),
                    credentials);
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentDestructionFailureException(e.getMessage());
        }

        try {
            contentRepository.deleteBySecret(secret.getId());
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentDestructionFailureException(e.getMessage());
        }
    }
}
