package com.repoachiever.resource.communication;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.service.communication.apiserver.IApiServerCommunicationService;
import com.repoachiever.service.integration.diagnostics.DiagnosticsConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Contains implementation of communication provider for RepoAchiever API Server.
 */
public class ApiServerCommunicationResource extends UnicastRemoteObject implements IApiServerCommunicationService {
    private static final Logger logger = LogManager.getLogger(ApiServerCommunicationResource.class);

    private final PropertiesEntity properties;

    public ApiServerCommunicationResource(PropertiesEntity properties) throws RemoteException {
        this.properties = properties;
    }

    /**
     * @see IApiServerCommunicationService
     */
    @Override
    public void performRawContentUpload(String workspaceUnitKey, InputStream content) throws RemoteException {

    }

    /**
     * @see IApiServerCommunicationService
     */
    @Override
    public void performAdditionalContentUpload(String workspaceUnitKey, String content) throws RemoteException {

    }

    /**
     * @see IApiServerCommunicationService
     */
    @Override
    public void performLogsTransfer(String name, String message) throws RemoteException {
        logger.info(String.format("Transferred logs(instance: %s): %s", name, message));
    }

    /**
     * @see IApiServerCommunicationService
     */
    @Override
    public Boolean retrieveHealthCheck() throws RemoteException {
        return true;
    }
}
