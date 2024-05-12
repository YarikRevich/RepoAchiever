package com.repoachiever.resource.communication;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.communication.cluster.IClusterCommunicationService;
import com.repoachiever.service.state.StateService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Contains implementation of communication provider for RepoAchiever Cluster.
 */
public class ClusterCommunicationResource extends UnicastRemoteObject implements IClusterCommunicationService {
    private final PropertiesEntity properties;

    public ClusterCommunicationResource(PropertiesEntity properties) throws RemoteException {
        this.properties = properties;
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public void performSuspend() throws RemoteException {
        StateService.setSuspended(true);
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public void performServe() throws RemoteException {
        StateService.setSuspended(false);
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public Boolean retrieveHealthCheck() throws RemoteException {
        return true;
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public String retrieveVersion() throws RemoteException {
        return properties.getGitCommitId();
    }

    @Override
    public Integer retrieveWorkerAmount() throws RemoteException {
        return 10;
    }
}