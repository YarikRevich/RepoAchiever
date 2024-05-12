package com.repoachiever.service.integration.communication.cluster.healthcheck;

import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.resource.communication.ApiServerCommunicationResource;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.cluster.facade.ClusterFacade;
import com.repoachiever.service.communication.common.CommunicationProviderConfigurationHelper;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service used to perform RepoAchiever Cluster communication health check operations.
 */
@Startup
@Priority(value = 210)
@ApplicationScoped
public class ClusterHealthCheckCommunicationService {
    private static final Logger logger = LogManager.getLogger(ClusterHealthCheckCommunicationService.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    ClusterFacade clusterFacade;

    private final static ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    /**
     * Performs RepoAchiever Cluster communication health check operations. If RepoAchiever Cluster is not responding,
     * then it will be redeployed.
     */
    @PostConstruct
    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                clusterFacade.reApplyUnhealthy();
            } catch (ClusterUnhealthyReapplicationFailureException e) {
                logger.fatal(e.getMessage());
            }
        }, 0, properties.getCommunicationClusterHealthCheckFrequency(), TimeUnit.MILLISECONDS);
    }
}