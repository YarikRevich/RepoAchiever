package com.repoachiever.resource.communication;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.service.communication.apiserver.IApiServerCommunicationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Contains implementation of communication provider for RepoAchiever API Server.
 */
public class ApiServerCommunicationResource extends UnicastRemoteObject implements IApiServerCommunicationService {
    private final PropertiesEntity properties;

    public ApiServerCommunicationResource(PropertiesEntity properties) throws RemoteException {
        this.properties = properties;
    }

    /**
     * @see IApiServerCommunicationService
     */
    @Override
    public Boolean retrieveHealthCheck() throws RemoteException {
        return true;
    }

    /**
     * @see IApiServerCommunicationService
     */
    @Override
    public String retrieveVersion() throws RemoteException {
        return properties.getGitCommitId();
    }
}
