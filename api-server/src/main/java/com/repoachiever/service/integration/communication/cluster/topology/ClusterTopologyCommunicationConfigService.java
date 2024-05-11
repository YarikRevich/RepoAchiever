package com.repoachiever.service.integration.communication.cluster.topology;

import com.repoachiever.exception.*;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.repository.facade.RepositoryFacade;
import com.repoachiever.service.cluster.facade.ClusterFacade;
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
        try {
            clusterFacade.destroyAll();
        } catch (ClusterFullDestructionFailureException e) {
            logger.error(e.getMessage());
        }
    }
}
