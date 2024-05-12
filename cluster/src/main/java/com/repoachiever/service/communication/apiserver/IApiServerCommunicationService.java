package com.repoachiever.service.communication.apiserver;

import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents communication provider for RepoAchiever API Server.
 */
public interface IApiServerCommunicationService extends Remote {
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
     * Handles incoming log messages related to the given RepoAchiever Cluster allocation.
     *
     * @param name    given RepoAchiever Cluster allocation name.
     * @param message given RepoAchiever Cluster log message.
     * @throws RemoteException if remote request fails.
     */
    void performLogsTransfer(String name, String message) throws RemoteException;

    /**
     * Retrieves latest RepoAchiever API Server health check states.
     *
     * @return RepoAchiever API Server health check status.
     * @throws RemoteException if remote request fails.
     */
    Boolean retrieveHealthCheck() throws RemoteException;
}