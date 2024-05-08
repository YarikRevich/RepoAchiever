package com.repoachiever.repository.facade;

import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.entity.repository.SecretEntity;
import com.repoachiever.exception.RepositoryApplicationFailureException;
import com.repoachiever.exception.RepositoryOperationFailureException;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentStateApplication;
import com.repoachiever.repository.ConfigRepository;
import com.repoachiever.repository.ContentRepository;
import com.repoachiever.repository.ProviderRepository;
import com.repoachiever.repository.SecretRepository;
import com.repoachiever.repository.common.RepositoryConfigurationHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
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
     * Applies given content application, updating previous state.
     *
     * @param contentApplication given content application used for topology configuration.
     * @throws RepositoryApplicationFailureException if RepoAchiever Cluster repository application failed.
     */
    public void apply(ContentApplication contentApplication) throws RepositoryApplicationFailureException {
        ProviderEntity provider;

        try {
            provider = providerRepository.findByName(contentApplication.getProvider().toString());
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryApplicationFailureException(e.getMessage());
        }

        Optional<String> credentials = RepositoryConfigurationHelper.getExternalCredentials(
                contentApplication.getProvider(), contentApplication.getCredentials().getExternal());

        try {
            if (!secretRepository.isPresentBySessionAndCredentials(
                    contentApplication.getCredentials().getInternal().getId(), credentials)) {
                secretRepository.insert(
                        contentApplication.getProvider().toString(),
                        contentApplication.getCredentials().getInternal().getId(),
                        credentials);
            }
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryApplicationFailureException(e.getMessage());
        }

        SecretEntity secret;

        try {
            secret = secretRepository.findBySessionAndCredentials(
                    contentApplication.getCredentials().getInternal().getId(),
                    credentials);
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryApplicationFailureException(e.getMessage());
        }

        try {
            contentRepository.deleteBySecret(secret.getId());
        } catch (RepositoryOperationFailureException e) {
            throw new RepositoryApplicationFailureException(e.getMessage());
        }

        for (String location : contentApplication.getLocations()) {
            try {
                contentRepository.insert(location, provider.getId(), secret.getId());
            } catch (RepositoryOperationFailureException e) {
                throw new RepositoryApplicationFailureException(e.getMessage());
            }
        }
    }
}