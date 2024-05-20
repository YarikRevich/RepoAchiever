package com.repoachiever.service.workspace.facade;

import com.repoachiever.entity.common.AdditionalContentFileEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.Provider;
import com.repoachiever.model.ContentCleanup;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.workspace.WorkspaceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Provides high-level access to workspace operations.
 */
@ApplicationScoped
public class WorkspaceFacade {
    @Inject
    ConfigService configService;

    @Inject
    WorkspaceService workspaceService;

    /**
     * Creates unit key with the help of the given readiness check application.
     *
     * @param provider          given provider.
     * @param credentialsFields given credentials.
     * @return result of the readiness check for the given configuration.
     */
    public String createUnitKey(Provider provider, CredentialsFieldsFull credentialsFields) {
        return switch (provider) {
            case EXPORTER -> workspaceService.createUnitKey(String.valueOf(credentialsFields.getInternal().getId()));
            case GIT_GITHUB -> workspaceService.createUnitKey(
                    String.valueOf(credentialsFields.getInternal().getId()), credentialsFields.getExternal().getToken());
        };
    }

    /**
     * Adds new version of raw content file as the raw input stream.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param name             given content name.
     * @param content          given content input.
     * @throws RawContentCreationFailureException if raw content creation operation failed.
     */
    public void addRawContent(String workspaceUnitKey, String location, String name, InputStream content)
            throws RawContentCreationFailureException {
        if (!workspaceService.isUnitDirectoryExist(workspaceUnitKey)) {
            try {
                workspaceService.createUnitDirectory(workspaceUnitKey);
            } catch (WorkspaceUnitDirectoryCreationFailureException e) {
                throw new RawContentCreationFailureException(e.getMessage());
            }
        }

        String workspaceUnitDirectory;

        try {
            workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
        } catch (WorkspaceUnitDirectoryNotFoundException e) {
            throw new RawContentCreationFailureException(e.getMessage());
        }

        if (!workspaceService.isRawContentUnitExist(workspaceUnitDirectory, location)) {
            try {
                workspaceService.createRawContentUnitDirectory(workspaceUnitDirectory, location);
            } catch (WorkspaceContentDirectoryCreationFailureException e) {
                throw new RawContentCreationFailureException(e.getMessage());
            }
        }

        if (!workspaceService.isRawContentDirectoryExist(workspaceUnitDirectory, location)) {
            try {
                workspaceService.createRawContentDirectory(workspaceUnitDirectory, location);
            } catch (WorkspaceContentDirectoryCreationFailureException e) {
                throw new RawContentCreationFailureException(e.getMessage());
            }
        }

        Integer amount;

        try {
            amount = workspaceService.getRawContentFilesAmount(workspaceUnitDirectory, location);
        } catch (RawContentFilesAmountRetrievalFailureException e) {
            throw new RawContentCreationFailureException(e);
        }

        try {
            workspaceService.createRawContentFile(workspaceUnitDirectory, location, name, content);
        } catch (RawContentFileWriteFailureException e) {
            throw new RawContentCreationFailureException(e.getMessage());
        }

        while (amount > configService.getConfig().getResource().getCluster().getMaxVersions() - 1) {
            try {
                workspaceService.removeEarliestRawContentFile(workspaceUnitDirectory, location);
            } catch (RawContentFileRemovalFailureException e) {
                throw new RawContentCreationFailureException(e.getMessage());
            }

            amount--;
        }
    }

    /**
     * Checks if raw content file exists with the given location and the given name.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param name             given content name.
     * @return result of the check.
     * @throws RawContentRetrievalFailureException if raw content retrieval operation failed.
     */
    public Boolean isRawContentPresent(String workspaceUnitKey, String location, String name) throws
            RawContentRetrievalFailureException {
        if (!workspaceService.isUnitDirectoryExist(workspaceUnitKey)) {
            return false;
        }

        String workspaceUnitDirectory;

        try {
            workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
        } catch (WorkspaceUnitDirectoryNotFoundException e) {
            throw new RawContentRetrievalFailureException(e.getMessage());
        }

        return workspaceService.isRawContentFileExist(workspaceUnitDirectory, location, name);
    }

    /**
     * Adds new version of raw content file as the raw input stream.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param name             given content name.
     * @param content          given content input.
     * @throws AdditionalContentCreationFailureException if additional content creation operation failed.
     */
    public void addAdditionalContent(
            String workspaceUnitKey, String location, String name, AdditionalContentFileEntity content) throws
            AdditionalContentCreationFailureException {
        if (!workspaceService.isUnitDirectoryExist(workspaceUnitKey)) {
            try {
                workspaceService.createUnitDirectory(workspaceUnitKey);
            } catch (WorkspaceUnitDirectoryCreationFailureException e) {
                throw new AdditionalContentCreationFailureException(e.getMessage());
            }
        }

        String workspaceUnitDirectory;

        try {
            workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
        } catch (WorkspaceUnitDirectoryNotFoundException e) {
            throw new AdditionalContentCreationFailureException(e.getMessage());
        }

        if (!workspaceService.isAdditionalContentUnitExist(workspaceUnitDirectory, location)) {
            try {
                workspaceService.createAdditionalContentUnitDirectory(workspaceUnitDirectory, location);
            } catch (WorkspaceContentDirectoryCreationFailureException e) {
                throw new AdditionalContentCreationFailureException(e.getMessage());
            }
        }

        if (!workspaceService.isAdditionalContentDirectoryExist(workspaceUnitDirectory, location)) {
            try {
                workspaceService.createAdditionalContentDirectory(workspaceUnitDirectory, location);
            } catch (WorkspaceContentDirectoryCreationFailureException e) {
                throw new AdditionalContentCreationFailureException(e.getMessage());
            }
        }

        Integer amount;

        try {
            amount = workspaceService.getRawContentFilesAmount(workspaceUnitDirectory, location);
        } catch (RawContentFilesAmountRetrievalFailureException e) {
            throw new AdditionalContentCreationFailureException(e);
        }

        try {
            workspaceService.createAdditionalContentFile(workspaceUnitDirectory, location, name, content);
        } catch (AdditionalContentFileWriteFailureException e) {
            throw new AdditionalContentCreationFailureException(e.getMessage());
        }

        while (amount > configService.getConfig().getResource().getCluster().getMaxVersions() - 1) {
            try {
                workspaceService.removeEarliestRawContentFile(workspaceUnitDirectory, location);
            } catch (RawContentFileRemovalFailureException e) {
                throw new AdditionalContentCreationFailureException(e.getMessage());
            }

            amount--;
        }
    }

    /**
     * Checks if additional content file exists with the given location and the given name.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @param name             given content name.
     * @return result of the check.
     * @throws AdditionalContentRetrievalFailureException if additional content retrieval operation failed.
     */
    public Boolean isAdditionalContentPresent(String workspaceUnitKey, String location, String name) throws
            AdditionalContentRetrievalFailureException {
        if (!workspaceService.isUnitDirectoryExist(workspaceUnitKey)) {
            return false;
        }

        String workspaceUnitDirectory;

        try {
            workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
        } catch (WorkspaceUnitDirectoryNotFoundException e) {
            throw new AdditionalContentRetrievalFailureException(e.getMessage());
        }

        return workspaceService.isAdditionalContentFileExist(workspaceUnitDirectory, location, name);
    }

    /**
     * Removes all the content from the workspace with the help of the given workspace unit key.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @throws ContentRemovalFailureException if content removal operation failed.
     */
    public void removeAll(String workspaceUnitKey) throws ContentRemovalFailureException {
        try {
            workspaceService.removeUnitDirectory(workspaceUnitKey);
        } catch (WorkspaceUnitDirectoryRemovalFailureException e) {
            throw new ContentRemovalFailureException(e.getMessage());
        }
    }

    /**
     * Creates content reference with the help of the given workspace unit key and content location.
     *
     * @param workspaceUnitKey given user workspace unit key.
     * @param location         given content location.
     * @return created content reference.
     * @throws ContentReferenceCreationFailureException if RepoAchiever Cluster content reference creation failed.
     */
    public byte[] createContentReference(String workspaceUnitKey, String location) throws
            ContentReferenceCreationFailureException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        try (ZipOutputStream writer = new ZipOutputStream(result)) {
            ZipEntry entry = new ZipEntry("test.txt");
            writer.putNextEntry(entry);
            writer.write("itworks".getBytes());
            writer.closeEntry();
            writer.flush();
            writer.finish();

        } catch (IOException e) {
            throw new ContentReferenceCreationFailureException(e.getMessage());
        }

        return result.toByteArray();
    }
}
