package com.repoachiever.resource.communication;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.service.communication.apiserver.IApiServerCommunicationService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.diagnostics.DiagnosticsConfigService;
import com.repoachiever.service.telemetry.TelemetryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Contains implementation of communication provider for RepoAchiever API Server.
 */
@ApplicationScoped
public class ApiServerCommunicationResource extends UnicastRemoteObject implements IApiServerCommunicationService {
    private static final Logger logger = LogManager.getLogger(ApiServerCommunicationResource.class);

    @Inject
    TelemetryService telemetryService;

    public ApiServerCommunicationResource() throws RemoteException {
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
        telemetryService.increaseApiServerHealthCheckAmount();

        Boolean result = true;

        telemetryService.decreaseApiServerHealthCheckAmount();

        return result;
    }
}
