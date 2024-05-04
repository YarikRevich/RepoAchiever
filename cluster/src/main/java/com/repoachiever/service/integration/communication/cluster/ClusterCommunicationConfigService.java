package com.repoachiever.service.integration.communication.cluster;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.resource.communication.ClusterCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.communication.common.CommunicationProviderConfigurationHelper;
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
public class ClusterCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(ClusterCommunicationConfigService.class);

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

        Thread.ofPlatform().start(() -> {
            try {
                registry.rebind(
                        CommunicationProviderConfigurationHelper.getBindName(
                                configService.getConfig().getCommunication().getPort(),
                                configService.getConfig().getName()),
                        new ClusterCommunicationResource(properties));
            } catch (RemoteException e) {
                logger.fatal(e.getMessage());
            }
        });
    }
}
