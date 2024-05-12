package com.repoachiever.service.communication.cluster;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Represents client for RepoAchiever Cluster remote API. */
public interface IClusterCommunicationService extends Remote {
    /**
     * Performs RepoAchiever Cluster suspend operation. Has no effect if RepoAchiever Cluster was
     * already suspended previously.
     *
     * @throws RemoteException if remote request fails.
     */
    void performSuspend() throws RemoteException;

    /**
     * Performs RepoAchiever Cluster serve operation. Has no effect if RepoAchiever Cluster was not
     * suspended previously.
     *
     * @throws RemoteException if remote request fails.
     */
    void performServe() throws RemoteException;

    /**
     * Retrieves latest RepoAchiever Cluster health check states.
     *
     * @return RepoAchiever Cluster health check status.
     * @throws RemoteException if remote request fails.
     */
    Boolean retrieveHealthCheck() throws RemoteException;

    /**
     * Retrieves version of the allocated RepoAchiever Cluster instance allowing to confirm API
     * compatability.
     *
     * @return RepoAchiever Cluster version.
     * @throws RemoteException if remote request fails.
     */
    String retrieveVersion() throws RemoteException;

    /**
     * Retrieves amount of allocated workers.
     *
     * @return amount of allocated workers.
     * @throws RemoteException if remote request fails.
     */
    Integer retrieveWorkerAmount() throws RemoteException;
}

// TODO: LOCATE ALL RMI RELATED CLASSES AT THE SAME PATH