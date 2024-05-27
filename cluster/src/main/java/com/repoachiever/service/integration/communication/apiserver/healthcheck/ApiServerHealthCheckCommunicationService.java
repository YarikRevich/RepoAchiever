package com.repoachiever.service.integration.communication.apiserver.healthcheck;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.service.apiserver.resource.ApiServerCommunicationResource;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service used to perform RepoAchiever Cluster communication health check operations.
 */
@Component
public class ApiServerHealthCheckCommunicationService {
    private static final Logger logger = LogManager.getLogger(ApiServerHealthCheckCommunicationService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ApiServerCommunicationResource apiServerCommunicationResource;

    private final static ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    /**
     * Performs RepoAchiever API Server communication health check operations. If RepoAchiever API Server is not responding,
     * then it's execution will be finished.
     */
    @PostConstruct
    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                if (!apiServerCommunicationResource.retrieveHealthCheck()) {
                    ((ConfigurableApplicationContext) applicationContext).close();
                    System.exit(1);
                }

            } catch (ApiServerOperationFailureException e) {
                ((ConfigurableApplicationContext) applicationContext).close();
                System.exit(1);
            }
        }, 0, properties.getCommunicationApiServerHealthCheckFrequency(), TimeUnit.MILLISECONDS);
    }
}