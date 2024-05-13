package com.repoachiever.resource.communication;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.communication.cluster.IClusterCommunicationService;
import com.repoachiever.service.state.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Contains implementation of communication provider for RepoAchiever Cluster.
 */
@Component
public class ClusterCommunicationResource extends UnicastRemoteObject implements IClusterCommunicationService {
    @Autowired
    private PropertiesEntity properties;

    public ClusterCommunicationResource() throws RemoteException {
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