package com.repoachiever.repository.facade;

import com.repoachiever.dto.RepositoryContentUnitDto;
import com.repoachiever.entity.repository.ContentEntity;
import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.entity.repository.SecretEntity;
import com.repoachiever.exception.ContentApplicationRetrievalFailureException;
import com.repoachiever.exception.RepositoryContentApplicationFailureException;
import com.repoachiever.exception.RepositoryContentDestructionFailureException;
import com.repoachiever.exception.RepositoryOperationFailureException;
import com.repoachiever.model.*;
import com.repoachiever.repository.ConfigRepository;
import com.repoachiever.repository.ContentRepository;
import com.repoachiever.repository.ProviderRepository;
import com.repoachiever.repository.SecretRepository;
import com.repoachiever.repository.common.RepositoryConfigurationHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     * Retrieves all the data from content repository in a form of content applications.
     *
     * @return retrieved set of content applications.
     * @throws ContentApplicationRetrievalFailureException if content application retrieval fails.
     */
    public List<ContentApplication> retrieveContentApplication() throws ContentApplicationRetrievalFailureException {
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

            SecretEntity rawSecret;

            try {
                rawSecret = secretRepository.findById(content.getSecret());
            } catch (RepositoryOperationFailureException e) {
                throw new ContentApplicationRetrievalFailureException(e.getMessage());
            }

            Provider provider =
                    RepositoryConfigurationHelper.convertRawProviderToContentProvider(
                            rawProvider.getName());

            CredentialsFieldsFull credentials =
                    RepositoryConfigurationHelper.convertRawSecretsToContentCredentials(
                            provider, rawSecret.getSession(), rawSecret.getCredentials());

            units.add(RepositoryContentUnitDto.of(
                    content.getLocation(),
                    provider,
                    credentials));
        }

        return units
                .stream()
                .map(element1 -> {
                    List<String> locations = units
                            .stream()
                            .filter(
                                    element2 -> Objects.equals(element1.getCredentials(), element2.getCredentials()))
                            .map(RepositoryContentUnitDto::getLocation)
                            .toList();

                    return ContentApplication.of(locations, element1.getProvider(), element1.getCredentials());
                })
                .toList();
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

        try {
            contentRepository.deleteBySecret(secret.getId());
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryContentApplicationFailureException(e.getMessage());
        }

        for (String location : contentApplication.getLocations()) {
            try {
                contentRepository.insert(location, provider.getId(), secret.getId());
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
