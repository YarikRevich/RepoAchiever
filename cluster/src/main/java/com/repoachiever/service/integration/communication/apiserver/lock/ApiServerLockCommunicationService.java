package com.repoachiever.service.integration.communication.apiserver.lock;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.service.apiserver.resource.ApiServerCommunicationResource;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service used to perform RepoAchiever Cluster communication lock operation.
 */
@Component
public class ApiServerLockCommunicationService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApiServerCommunicationResource apiServerCommunicationResource;

    /**
     * Performs RepoAchiever API Server communication lock operation. If RepoAchiever Cluster allocation is locked,
     * then application start is interrupted.
     */
    @PostConstruct
    private void process() {
        try {
            if (apiServerCommunicationResource.retrieveClusterAllocationLocked()) {
                ((ConfigurableApplicationContext) applicationContext).close();
                System.exit(1);
            }

        } catch (ApiServerOperationFailureException e) {
            ((ConfigurableApplicationContext) applicationContext).close();
            System.exit(1);
        }

        try {
            apiServerCommunicationResource.performLockClusterAllocation();
        } catch (ApiServerOperationFailureException e) {
            ((ConfigurableApplicationContext) applicationContext).close();
            System.exit(1);
        }
    }
}