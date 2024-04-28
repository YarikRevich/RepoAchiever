package com.repoachiever.resource.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Represents client for RepoAchiever Cluster remote API. */
public interface ICommunicationService extends Remote {
    /**
     * Retrieves latest health check states.
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