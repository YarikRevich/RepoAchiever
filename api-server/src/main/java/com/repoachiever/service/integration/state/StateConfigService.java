package com.repoachiever.service.integration.state;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ApiServerInstanceIsAlreadyRunningException;
import com.repoachiever.service.state.StateService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Service used to perform application critical state configuration.
 */
@Startup
@Priority(value = 140)
@ApplicationScoped
public class StateConfigService {
    private static final Logger logger = LogManager.getLogger(StateConfigService.class);

    @Inject
    PropertiesEntity properties;

    /**
     * Performs application state initialization operations.
     */
    @PostConstruct
    private void process() {
        Path running = Paths.get(properties.getStateLocation(), properties.getStateRunningName());

        if (Files.exists(running)) {
            logger.fatal(new ApiServerInstanceIsAlreadyRunningException().getMessage());
            return;
        }

        try {
            Files.createFile(running);
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }

        StateService.setStarted(true);
    }

    /**
     * Performs graceful application state cleanup after execution is finished.
     */
    @PreDestroy
    private void close() {
        if (StateService.getStarted()) {
            try {
                Files.delete(Paths.get(properties.getStateLocation(), properties.getStateRunningName()));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
