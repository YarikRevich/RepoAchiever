package com.repoachiever.service.cluster.resource;

import com.repoachiever.exception.ClusterOperationFailureException;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import com.repoachiever.service.communication.cluster.IClusterCommunicationService;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Represents implementation for RepoAchiever Cluster remote API.
 */
@ApplicationScoped
public class ClusterClientResource {
    private static final Logger logger = LogManager.getLogger(ClusterClientResource.class);

    @Inject
    ConfigService configService;

    private Registry registry;

    @PostConstruct
    private void configure() {
        try {
            this.registry = LocateRegistry.getRegistry(
                    configService.getConfig().getCommunication().getPort());
        } catch (RemoteException e) {
            logger.fatal(new CommunicationConfigurationFailureException(e.getMessage()).getMessage());
        }
    }

    /**
     * Retrieves health check status of the RepoAchiever Cluster with the given name.
     *
     * @param name given name of RepoAchiever Cluster.
     * @return result of the check.
     */
    public Boolean retrieveHealthCheck(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation;

        try {
            allocation = (IClusterCommunicationService) registry.lookup(name);
        } catch (RemoteException | NotBoundException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }

        System.out.println(name);
        try {
            System.out.println(allocation.retrieveWorkerAmount());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        try {
            return allocation.retrieveHealthCheck();
        } catch (RemoteException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves version of the RepoAchiever Cluster with the given name.
     *
     * @param name given name of RepoAchiever Cluster.
     * @return retrieved version of RepoAchiever Cluster.
     */
    public String retrieveVersion(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation;

        try {
            allocation = (IClusterCommunicationService) registry.lookup(name);
        } catch (RemoteException | NotBoundException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }

        try {
            return allocation.retrieveVersion();
        } catch (RemoteException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves amount of RepoAchiever Worker owned by RepoAchiever Cluster with the given name.
     *
     * @param name given name of RepoAchiever Cluster.
     * @return retrieved amount of RepoAchiever Worker owned by RepoAchiever Cluster allocation.
     */
    public Integer retrieveWorkerAmount(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation;

        try {
            allocation = (IClusterCommunicationService) registry.lookup(name);
        } catch (RemoteException | NotBoundException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }

        try {
            return allocation.retrieveWorkerAmount();
        } catch (RemoteException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }
}
