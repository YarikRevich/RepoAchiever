package com.repoachiever.service.workspace.facade;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.CredentialsFields;
import com.repoachiever.model.Provider;
import com.repoachiever.service.workspace.WorkspaceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;

/**
 * Provides high-level access to workspace operations.
 */
@ApplicationScoped
public class WorkspaceFacade {
    @Inject
    PropertiesEntity properties;

    @Inject
    WorkspaceService workspaceService;

    /**
     * Creates unit key with the help of the given readiness check application.
     *
     * @param id                given session identificator.
     * @param provider          given provider.
     * @param credentialsFields given credentials.
     * @return result of the readiness check for the given configuration.
     */
    public String createUnitKey(String id, Provider provider, CredentialsFields credentialsFields) {
        return switch (provider) {
            case LOCAL -> workspaceService.createUnitKey(id);
            case GITHUB -> workspaceService.createUnitKey(id, credentialsFields.getToken());
        };
    }


    public void addContentVersion(String token) {

    }

    public void updatePRsMetadataFile() {

    }

    public void updateIssuesMetadataFile() {
    }

    public void updateReleasesMetadataFile() {
    }

//    /**
//     * Update Kafka host in internal config file.
//     *
//     * @param provider          given provider.
//     * @param credentialsFields given credentials.
//     * @throws WorkspaceUnitDirectoryNotFoundException if workspace unit directory was not found.
//     * @throws InternalConfigNotFoundException         if internal config file was not found.
//     * @throws InternalConfigWriteFailureException     if internal config file cannot be created.
//     */
//    public void updateKafkaHost(
//            String machineAdWorkspaceUnitDirectoryNotFoundExceptiondress, Provider provider, CredentialsFields credentialsFields)
//            throws,
//            InternalConfigNotFoundException,
//            InternalConfigWriteFailureException {
//        String workspaceUnitKey = createUnitKey(provider, credentialsFields);
//
//        String workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
//
//        InternalConfigEntity internalConfig = null;
//
//        if (workspaceService.isInternalConfigFileExist(workspaceUnitDirectory)) {
//            internalConfig = workspaceService.getInternalConfigFileContent(workspaceUnitDirectory);
//        }
//
//        if (Objects.isNull(internalConfig)) {
//            internalConfig = new InternalConfigEntity();
//        }
//
//        internalConfig.getKafka().setHost(machineAddress);
//
//        workspaceService.createInternalConfigFile(workspaceUnitDirectory, internalConfig);
//    }
}
