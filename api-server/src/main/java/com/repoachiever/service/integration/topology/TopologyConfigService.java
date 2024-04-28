package com.repoachiever.service.integration.topology;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service used to perform topology configuration.
 */
@Startup(value = 250)
@ApplicationScoped
public class TopologyConfigService {
    private static final Logger logger = LogManager.getLogger(TopologyConfigService.class);

    /**
     * Recreates previously created topology infrastructure if such existed before.
     */
    @PostConstruct
    private void process() {
        // TODO: retrieve registered locations from the database and create cluster instances.
    }

    /**
     * Gracefully stops all the created topology infrastructure.
     */
    @PreDestroy
    private void close() {

    }
}
