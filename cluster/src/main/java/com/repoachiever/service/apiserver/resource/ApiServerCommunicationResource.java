package com.repoachiever.service.apiserver.resource;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.SimpleRemoteInputStream;
import com.repoachiever.converter.AdditionalContentDataToJsonConverter;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.CommunicationConfigurationFailureException;
import com.repoachiever.service.communication.apiserver.IApiServerCommunicationService;
import com.repoachiever.service.communication.common.CommunicationProviderConfigurationHelper;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Map;

/**
 * Represents implementation for RepoAchiever API Server remote API.
 */
@Service
public class ApiServerCommunicationResource {
    @Autowired
    private ConfigService configService;

    private Registry registry;

    /**
     * Configures RepoAchiever API Server communication registry.
     *
     * @throws CommunicationConfigurationFailureException if RepoAchiever API Server communication failed.
     */
    @PostConstruct
    private void configure() throws CommunicationConfigurationFailureException {
        try {
            this.registry = LocateRegistry.getRegistry(
                    configService.getConfig().getCommunication().getPort());
        } catch (RemoteException e) {
            throw new CommunicationConfigurationFailureException(e.getMessage());
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
     * @param content  given content to be uploaded.
     * @param location given content location.
     * @param name     given content name.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performRawContentUpload(String location, String name, DataBuffer content) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        RemoteInputStream contentWrapped;

        try {
            contentWrapped = new SimpleRemoteInputStream(content.asInputStream())
                    .export();
        } catch (RemoteException e) {
            DataBufferUtils.release(content);

            throw new ApiServerOperationFailureException(e.getMessage());
        }

        try {
            allocation.performRawContentUpload(
                    configService.getConfig().getMetadata().getWorkspaceUnitKey(),
                    location,
                    name,
                    contentWrapped);
        } catch (RemoteException e) {
            DataBufferUtils.release(content);

            throw new ApiServerOperationFailureException(e.getMessage());
        }

        DataBufferUtils.release(content);
    }

    /**
     * Checks if raw content with the given value at the given location is already present.
     *
     * @param location given content location.
     * @param name     given content name.
     * @return result of the check.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public Boolean retrieveRawContentPresent(String location, String name) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            return allocation.retrieveRawContentPresent(
                    configService.getConfig().getMetadata().getWorkspaceUnitKey(),
                    location,
                    name);
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Performs additional content(issues, prs, releases) upload operation, initiated by RepoAchiever Cluster.
     *
     * @param location given content location.
     * @param name     given content name.
     * @param data  given data to be uploaded.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performAdditionalContentUpload(
            String location, String name, Map<String, String> data) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performAdditionalContentUpload(
                    configService.getConfig().getMetadata().getWorkspaceUnitKey(),
                    location,
                    name,
                    AdditionalContentDataToJsonConverter.convert(data));
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Checks if additional content with the given value at the given location is already present.
     *
     * @param location given content location.
     * @param name     given content name.
     * @return result of the check.
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public Boolean retrieveAdditionalContentPresent(String location, String name) throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            return allocation.retrieveAdditionalContentPresent(
                    configService.getConfig().getMetadata().getWorkspaceUnitKey(),
                    location,
                    name);
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
     * Handles incoming download telemetry amount increase.
     *
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performDownloadTelemetryIncrease() throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performDownloadTelemetryIncrease();
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Handles incoming download telemetry amount decrease.
     *
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performDownloadTelemetryDecrease() throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performDownloadTelemetryDecrease();
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Handles RepoAchiever Cluster allocation lock.
     *
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public void performLockClusterAllocation() throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            allocation.performLockClusterAllocation(configService.getConfig().getMetadata().getName());
        } catch (RemoteException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves RepoAchiever Cluster allocation lock state.
     *
     * @throws ApiServerOperationFailureException if RepoAchiever API Server operation fails.
     */
    public Boolean retrieveClusterAllocationLocked() throws ApiServerOperationFailureException {
        IApiServerCommunicationService allocation = retrieveAllocation();

        try {
            return allocation.retrieveClusterAllocationLocked(configService.getConfig().getMetadata().getName());
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
