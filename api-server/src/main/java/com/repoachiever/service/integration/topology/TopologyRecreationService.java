package com.repoachiever.service.integration.topology;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides http server configuration used as a source of properties for all defined resources.
 */
@ApplicationScoped
public class TopologyRecreationService {
    private static final Logger logger = LogManager.getLogger(TopologyRecreationService.class);

    /**
     * Recreates previously generated topology if such existed before.
     */
    @PostConstruct
    private void process() {
    }
}
