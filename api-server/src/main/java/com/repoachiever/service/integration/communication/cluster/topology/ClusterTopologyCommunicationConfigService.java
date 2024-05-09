package com.repoachiever.service.integration.communication.cluster.topology;

import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.exception.ClusterDestructionFailureException;
import com.repoachiever.repository.ConfigRepository;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.state.StateService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Shutdown;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service used to perform topology configuration.
 */
@Startup(value = 170)
@ApplicationScoped
public class ClusterTopologyCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(ClusterTopologyCommunicationConfigService.class);

    @Inject
    ConfigRepository configRepository;

    @Inject
    ClusterService clusterService;

    /**
     * Recreates previously created topology infrastructure if such existed before.
     */
    @PostConstruct
    private void process() {
        // TODO: retrieve registered locations from the database and create cluster instances.

//        try {
//            clusterService.deploy();
//        } catch (ClusterDeploymentFailureException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            configRepository.insert("test", "itworks");
//        } catch (RepositoryOperationFailureException e) {
//            throw new RuntimeException(e);
//        }
//
//        try {
//            System.out.println(configRepository.findByName("test").getHash());
//        } catch (RepositoryOperationFailureException e) {
//            throw new RuntimeException(e);
//        }
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
