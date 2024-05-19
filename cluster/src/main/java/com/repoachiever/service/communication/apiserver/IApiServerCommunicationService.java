package com.repoachiever.service.communication.apiserver;

import com.healthmarketscience.rmiio.RemoteInputStream;

import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Represents communication provider for RepoAchiever API Server.
 */
public interface IApiServerCommunicationService extends Remote {
    /**
     * Performs raw content upload operation at the given location with the given name, initiated by RepoAchiever Cluster.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param name             given content name.
     * @param content          given content to be uploaded.
     * @throws RemoteException if remote request fails.
     */
    void performRawContentUpload(String workspaceUnitKey, String location, String name, RemoteInputStream content)
            throws RemoteException;

    /**
     * Checks if raw content with the given value at the given location is already present.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param value            given content name.
     * @return result of the check.
     * @throws RemoteException if remote request fails
     */
    Boolean retrieveRawContentPresent(String workspaceUnitKey, String location, String value) throws RemoteException;

    /**
     * Performs additional content(issues, prs, releases) upload operation at the given location with the given name,
     * initiated by RepoAchiever Cluster.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param name             given content name.
     * @param content          given content to be uploaded.
     * @throws RemoteException if remote request fails.
     */
    void performAdditionalContentUpload(String workspaceUnitKey, String location, String name, String content)
            throws RemoteException;

    /**
     * Checks if additional content with the given value at the given location is already present.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param value            given content name.
     * @return result of the check.
     * @throws RemoteException if remote request fails
     */
    Boolean retrieveAdditionalContentPresent(String workspaceUnitKey, String location, String value)
            throws RemoteException;

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