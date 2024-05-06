package com.repoachiever.service.communication.apiserver;

import java.rmi.RemoteException;

/**
 * Represents communication provider for RepoAchiever API Server.
 */
public interface IApiServerCommunicationService {
    /**
     * Retrieves latest RepoAchiever API Server health check states.
     *
     * @return RepoAchiever API Server health check status.
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
}