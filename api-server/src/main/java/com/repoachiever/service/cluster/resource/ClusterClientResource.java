package com.repoachiever.service.cluster.resource;

import com.repoachiever.exception.ClusterOperationFailureException;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.service.communication.common.CommunicationProviderConfigurationHelper;
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
     * Retrieves remote RepoAchiever Cluster allocation with the given name.
     *
     * @param name given RepoAchiever Cluster allocation name.
     * @return retrieved RepoAchiever Cluster allocation.
     * @throws ClusterOperationFailureException if RepoAchiever Cluster operation fails.
     */
    private IClusterCommunicationService retrieveAllocation(String name) throws ClusterOperationFailureException {
        try {
            return (IClusterCommunicationService) registry.lookup(
                    CommunicationProviderConfigurationHelper.getBindName(
                            configService.getConfig().getCommunication().getPort(),
                            name));
        } catch (RemoteException | NotBoundException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }

    /**
     * Performs RepoAchiever Cluster suspend operation. Has no effect if RepoAchiever Cluster was already suspended
     * previously.
     *
     * @param name given name of RepoAchiever Cluster.
     * @throws ClusterOperationFailureException if RepoAchiever Cluster operation fails.
     */
    public void performSuspend(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation = retrieveAllocation(name);

        try {
            allocation.performSuspend();
        } catch (RemoteException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }

    /**
     * Performs RepoAchiever Cluster serve operation. Has no effect if RepoAchiever Cluster was not suspended previously.
     *
     * @param name given name of RepoAchiever Cluster.
     * @throws ClusterOperationFailureException if RepoAchiever Cluster operation fails.
     */
    public void performServe(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation = retrieveAllocation(name);

        try {
            allocation.performServe();
        } catch (RemoteException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves health check status of the RepoAchiever Cluster with the given name.
     *
     * @param name given name of RepoAchiever Cluster.
     * @return result of the check.
     * @throws ClusterOperationFailureException if RepoAchiever Cluster operation fails.
     */
    public Boolean retrieveHealthCheck(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation = retrieveAllocation(name);

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
     * @throws ClusterOperationFailureException if RepoAchiever Cluster operation fails.
     */
    public String retrieveVersion(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation = retrieveAllocation(name);

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
     * @throws ClusterOperationFailureException if RepoAchiever Cluster operation fails.
     */
    public Integer retrieveWorkerAmount(String name) throws ClusterOperationFailureException {
        IClusterCommunicationService allocation = retrieveAllocation(name);

        try {
            return allocation.retrieveWorkerAmount();
        } catch (RemoteException e) {
            throw new ClusterOperationFailureException(e.getMessage());
        }
    }
}
