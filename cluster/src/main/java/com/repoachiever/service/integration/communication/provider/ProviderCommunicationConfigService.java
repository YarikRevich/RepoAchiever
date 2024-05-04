package com.repoachiever.service.integration.communication.provider;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.resource.communication.ClusterCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.communication.common.ProviderCommunicationConfigurationHelper;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(ProviderCommunicationConfigService.class);

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

        System.out.println(configService.getConfig().getCommunication().getPort());
        System.out.println(configService.getConfig().getName());

        Thread.ofPlatform().start(() -> {
            try {
                registry.rebind(
                        ProviderCommunicationConfigurationHelper.getBindName(
                                configService.getConfig().getCommunication().getPort(),
                                configService.getConfig().getName()),
                        new ClusterCommunicationResource(properties));
            } catch (RemoteException e) {
                logger.fatal(e.getMessage());
            }
        });
    }
}
