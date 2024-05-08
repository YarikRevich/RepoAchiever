package com.repoachiever.service.communication.apiserver;

import java.io.InputStream;
import java.rmi.RemoteException;

/**
 * Represents communication provider for RepoAchiever API Server.
 */
public interface IApiServerCommunicationService {
    /**
     * Performs raw content upload operation, initiated by RepoAchiever Cluster.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param content given content to be uploaded.
     * @throws RemoteException if remote request fails.
     */
    void performRawContentUpload(String workspaceUnitKey, InputStream content) throws RemoteException;

    /**
     * Performs additional content(issues, prs, releases) upload operation, initiated by RepoAchiever Cluster.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param content given content to be uploaded.
     * @throws RemoteException if remote request fails.
     */
    void performAdditionalContentUpload(String workspaceUnitKey, String content) throws RemoteException;

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