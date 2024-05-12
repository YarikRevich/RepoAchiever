package com.repoachiever.service.integration.communication.registry;

import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.service.config.ConfigService;
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
 * Service used to perform initial communication infrastructure configuration.
 */
@Startup(value = 150)
@ApplicationScoped
public class RegistryCommunicationConfigService {
    private static final Logger logger = LogManager.getLogger(RegistryCommunicationConfigService.class);

    @Inject
    ConfigService configService;

    /**
     * Performs initial communication infrastructure configuration.
     */
    @PostConstruct
    private void process() {
        Registry registry;

        try {
            registry = LocateRegistry.createRegistry(
                    configService.getConfig().getCommunication().getPort());
        } catch (RemoteException e) {
            logger.fatal(new CommunicationConfigurationFailureException(e.getMessage()).getMessage());
            return;
        }

        try {
            registry.list();
        } catch (RemoteException e) {
            logger.fatal(new CommunicationConfigurationFailureException(e.getMessage()).getMessage());
        }
    }
}
