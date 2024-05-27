package com.repoachiever.service.integration.communication.cluster.topology;

import com.repoachiever.exception.*;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.repository.facade.RepositoryFacade;
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
@Startup(value = 400)
@ApplicationScoped
public class ClusterTopologyCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(ClusterTopologyCommunicationConfigService.class);

    @Inject
    RepositoryFacade repositoryFacade;

    @Inject
    ClusterFacade clusterFacade;

    /**
     * Recreates previously created topology infrastructure if such existed before.
     *
     * @throws ApplicationStartGuardFailureException           if RepoAchiever API Server application start guard operation
     *                                                         fails.
     * @throws ClusterStartTopologyApplicationFailureException if RepoAchiever API Server start topology application
     *                                                         fails.
     */
    @PostConstruct
    private void process() throws
            ApplicationStartGuardFailureException,
            ClusterStartTopologyApplicationFailureException {
        try {
            StateService.getStartGuard().await();
        } catch (InterruptedException e) {
            throw new ApplicationStartGuardFailureException(e.getMessage());
        }

        List<ContentApplication> applications;

        try {
            applications = repositoryFacade.retrieveContentApplication();
        } catch (ContentApplicationRetrievalFailureException ignored) {
            return;
        }

        for (ContentApplication application : applications) {
            try {
                clusterFacade.apply(application);
            } catch (ClusterApplicationFailureException e) {
                throw new ClusterStartTopologyApplicationFailureException(e.getMessage());
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
