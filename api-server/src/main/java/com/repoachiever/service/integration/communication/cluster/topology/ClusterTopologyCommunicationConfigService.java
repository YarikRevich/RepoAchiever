package com.repoachiever.service.integration.communication.cluster.topology;

import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.entity.repository.ContentEntity;
import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.entity.repository.SecretEntity;
import com.repoachiever.exception.ClusterApplicationFailureException;
import com.repoachiever.exception.ClusterDestructionFailureException;
import com.repoachiever.exception.ContentApplicationRetrievalFailureException;
import com.repoachiever.exception.RepositoryOperationFailureException;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.repository.ConfigRepository;
import com.repoachiever.repository.ContentRepository;
import com.repoachiever.repository.ProviderRepository;
import com.repoachiever.repository.SecretRepository;
import com.repoachiever.repository.facade.RepositoryFacade;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.cluster.facade.ClusterFacade;
import com.repoachiever.service.state.StateService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Shutdown;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Service used to perform topology configuration.
 */
@Startup(value = 170)
@ApplicationScoped
public class ClusterTopologyCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(ClusterTopologyCommunicationConfigService.class);

    @Inject
    RepositoryFacade repositoryFacade;

    @Inject
    ClusterFacade clusterFacade;

    @Inject
    ClusterService clusterService;

    /**
     * Recreates previously created topology infrastructure if such existed before.
     */
    @PostConstruct
    private void process() {
        List<ContentApplication> applications;

        try {
            applications = repositoryFacade.retrieveContentApplication();
        } catch (ContentApplicationRetrievalFailureException e) {
            logger.fatal(e.getMessage());
            return;
        }

        for (ContentApplication application : applications) {
            try {
                clusterFacade.apply(application);
            } catch (ClusterApplicationFailureException e) {
                logger.fatal(e.getMessage());
                return;
            }
        }
    }

    /**
     * Gracefully stops all the created topology infrastructure.
     */
    public void close(@Observes Shutdown event) {
        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException ignored) {
            }
        }
    }
}
