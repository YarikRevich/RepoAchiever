package com.repoachiever.service.integration.communication.provider;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Service used to perform RepoAchiever Cluster communication provider configuration.
 */
@Component
public class ProviderCommunicationConfigService {
    @Autowired
    private ConfigService configService;

    @Autowired
    private PropertiesEntity properties;

    /**
     * Performs setup of RepoAchiever Cluster communication provider.
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
                        new CommunicationProviderResource(properties));
            } catch (RemoteException e) {
                logger.fatal(e.getMessage());
            }
        });
    }
}
