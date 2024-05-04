package com.repoachiever.service.integration.topology;

import com.repoachiever.exception.ClusterDeploymentFailureException;
import com.repoachiever.repository.ConfigRepository;
import com.repoachiever.service.cluster.ClusterService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service used to perform topology configuration.
 */
@Startup(value = 250)
@ApplicationScoped
public class TopologyConfigService {
    private static final Logger logger = LogManager.getLogger(TopologyConfigService.class);

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
    @PreDestroy
    private void close() {

    }
}
