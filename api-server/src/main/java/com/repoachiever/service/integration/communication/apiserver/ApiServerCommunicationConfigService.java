package com.repoachiever.service.integration.communication.apiserver;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ApplicationStartGuardFailureException;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.resource.communication.ApiServerCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.communication.common.CommunicationProviderConfigurationHelper;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.telemetry.TelemetryService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Service used to perform RepoAchiever API Server communication provider configuration.
 */
@Startup(value = 300)
@ApplicationScoped
public class ApiServerCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(ApiServerCommunicationConfigService.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    @Inject
    ApiServerCommunicationResource apiServerCommunicationResource;

    /**
     * Performs setup of RepoAchiever API Server communication provider.
     *
     * @throws ApplicationStartGuardFailureException      if RepoAchiever API Server application start guard operation
     *                                                    fails.
     * @throws CommunicationConfigurationFailureException if RepoAchiever API Server communication configuration fails.
     */
    @PostConstruct
    private void process() throws
            ApplicationStartGuardFailureException,
            CommunicationConfigurationFailureException {
        try {
            StateService.getStartGuard().await();
        } catch (InterruptedException e) {
            throw new ApplicationStartGuardFailureException(e.getMessage());
        }

        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(
                    configService.getConfig().getCommunication().getPort());
        } catch (RemoteException e) {
            throw new CommunicationConfigurationFailureException(e.getMessage());
        }

        Thread.ofPlatform().start(() -> {
            try {
                registry.rebind(
                        CommunicationProviderConfigurationHelper.getBindName(
                                configService.getConfig().getCommunication().getPort(),
                                properties.getCommunicationApiServerName()),
                        apiServerCommunicationResource);
            } catch (RemoteException e) {
                logger.fatal(e.getMessage());
            }
        });
    }
}