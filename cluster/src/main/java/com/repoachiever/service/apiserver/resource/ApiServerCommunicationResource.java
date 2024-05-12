package com.repoachiever.service.apiserver.resource;

import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.service.communication.apiserver.IApiServerCommunicationService;
import com.repoachiever.service.communication.common.CommunicationProviderConfigurationHelper;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

/**
 * Represents implementation for RepoAchiever API Server remote API.
 */
@Service
public class ApiServerCommunicationResource {
    private static final Logger logger = LogManager.getLogger(ApiServerCommunicationResource.class);

    @Autowired
    private ConfigService configService;

    private Registry registry;

    @PostConstruct
    private void configure() {
        try {
            this.registry = LocateRegistry.getRegistry(
                    configService.getConfig().getCommunication().getPort());
        } catch (RemoteException e) {
            logger.fatal(new CommunicationConfigurationFailureException(e.getMessage()).getMessage());
        }
    }

    /**
     * Retrieves remote RepoAchiever API Server allocation.
     *
     * @return retrieved RepoAchiever API Server allocation.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    private IApiServerCommunicationService retrieveAllocation() throws ApiServerOperationFailureException {
        try {
            return (IApiServerCommunicationService) registry.lookup(
                    CommunicationProviderConfigurationHelper.getBindName(
                            configService.getConfig().getCommunication().getPort(),
                            configService.getConfig().getCommunication().getApiServerName()));
        } catch (RemoteException | NotBoundException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Performs raw content upload operation.
     *
     * @param content given content to be uploaded.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performRawContentUpload(InputStream content) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performRawContentUpload(
                    configService.getConfig().getMetadata().getWorkspaceUnitKey(),
                    content);
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Performs additional content(issues, prs, releases) upload operation, initiated by RepoAchiever Cluster.
     *
     * @param content given content to be uploaded.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performAdditionalContentUpload(String content) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performAdditionalContentUpload(
                    configService.getConfig().getMetadata().getWorkspaceUnitKey(),
                    content);
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Handles incoming log messages related to the RepoAchiever Cluster allocation.
     *
     * @param message given RepoAchiever Cluster log message.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performLogsTransfer(String message) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performLogsTransfer(
                    configService.getConfig().getMetadata().getName(), message);
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves health check status of RepoAchiever API Server allocation.
     *
     * @return result of the check.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public Boolean retrieveHealthCheck() throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            return allocation.retrieveHealthCheck();
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }
}