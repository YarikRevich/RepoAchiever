package com.repoachiever.resource.communication.provider;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.resource.communication.ICommunicationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Contains implementation of communication provider for RepoAchiever API Server.
 */
public class CommunicationProviderResource extends UnicastRemoteObject implements ICommunicationService {
    private final PropertiesEntity properties;

    public CommunicationProviderResource(PropertiesEntity properties) throws RemoteException {
        this.properties = properties;
    }

    /**
     * @see ICommunicationService
     */
    @Override
    public Boolean retrieveHealthCheck() throws RemoteException {
        return true;
    }

    /**
     * @see ICommunicationService
     */
    @Override
    public String retrieveVersion() throws RemoteException {
        return properties.getGitCommitId();
    }
}
