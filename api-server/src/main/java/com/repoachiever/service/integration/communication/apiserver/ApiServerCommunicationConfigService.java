package com.repoachiever.service.integration.communication.apiserver;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.resource.communication.ApiServerCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.communication.apiserver.common.ProviderCommunicationConfigurationHelper;
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
@Startup(value = 200)
@ApplicationScoped
public class ApiServerCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(ApiServerCommunicationConfigService.class);

    @Inject
    ConfigService configService;

    @Inject
    PropertiesEntity properties;

    /**
     * Performs setup of RepoAchiever API Server communication provider.
     */
    @PostConstruct
    private void process() {
        Registry registry;

        try {
            registry = LocateRegistry.getRegistry(
                    configService.getConfig().getCommunication().getPort());
        } catch (RemoteException e) {
            logger.fatal(new CommunicationConfigurationFailureException(e.getMessage()).getMessage());
            return;
        }

        // TODO: add communication provider activity to global API SERVER health check

        Thread.ofPlatform().start(() -> {
            try {
                registry.rebind(
                        ProviderCommunicationConfigurationHelper.getBindName(
                                configService.getConfig().getCommunication().getPort(),
                                properties.getCommunicationProviderName()),
                        new ApiServerCommunicationResource(properties));
            } catch (RemoteException e) {
                logger.fatal(e.getMessage());
            }
        });
    }
}