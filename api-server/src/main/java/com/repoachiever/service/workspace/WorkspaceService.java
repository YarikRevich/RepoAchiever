package com.repoachiever.service.workspace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.entity.MetadataFileEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.xml.bind.DatatypeConverter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FileSystemUtils;

/**
 * Represents local content workspace for different users.
 */
@ApplicationScoped
public class WorkspaceService {
    private static final Logger logger = LogManager.getLogger(WorkspaceService.class);

    @Inject
    PropertiesEntity properties;

    /**
     * Creates unit key from the given segments.
     *
     * @param segments given segments to be used for unit key creation.
     * @return created unit key from the given segments.
     */
    @SneakyThrows
    public String createUnitKey(String... segments) {
        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        return DatatypeConverter.printHexBinary(md.digest(String.join(".", segments).getBytes()));
    }

    /**
     * Creates content directory in the given workspace unit directory
     *
     * @param workspaceUnitDirectory given workspace unit directory
     * @throws WorkspaceContentDirectoryCreationFailureException if workspace content directory creation operation failed.
     */
    public void createContentDirectory(String workspaceUnitDirectory) throws
            WorkspaceContentDirectoryCreationFailureException {
        Path unitDirectoryPath = Path.of(workspaceUnitDirectory, properties.getWorkspaceContentDirectory());

        if (Files.notExists(unitDirectoryPath)) {
            try {
                Files.createDirectory(unitDirectoryPath);
            } catch (IOException e) {
                throw new WorkspaceContentDirectoryCreationFailureException(e.getMessage());
            }
        }
    }

    /**
     * Creates workspace unit with the help of the given key.
     *
     * @param key given workspace unit key.
     * @throws WorkspaceUnitDirectoryCreationFailureException if workspace unit directory creation operation failed.
     */
    public void createUnitDirectory(String key) throws
            WorkspaceUnitDirectoryCreationFailureException {
        Path unitDirectoryPath = Path.of(properties.getWorkspaceDirectory(), key);

        if (Files.notExists(unitDirectoryPath)) {
            try {
                Files.createDirectory(unitDirectoryPath);
            } catch (IOException e) {
                throw new WorkspaceUnitDirectoryCreationFailureException(e.getMessage());
            }
        }
    }

    /**
     * Removes workspace unit with the help of the given key.
     *
     * @param key given workspace unit key.
     * @throws WorkspaceUnitDirectoryRemovalFailureException if IO operation failed.
     */
    public void removeUnitDirectory(String key) throws WorkspaceUnitDirectoryRemovalFailureException {
        try {
            FileSystemUtils.deleteRecursively(Path.of(properties.getWorkspaceDirectory(), key));
        } catch (IOException e) {
            throw new WorkspaceUnitDirectoryRemovalFailureException(e.getMessage());
        }
    }

    /**
     * Checks if workspace unit directory with the help of the given key.
     *
     * @param key given workspace unit key.
     * @return result if workspace unit directory exists with the help of the given key.
     */
    public boolean isUnitDirectoryExist(String key) {
        return Files.exists(Paths.get(properties.getWorkspaceDirectory(), key));
    }

    /**
     * Retrieves path for the workspace unit with the help of the given key.
     *
     * @param key given workspace unit key.
     * @throws WorkspaceUnitDirectoryNotFoundException if workspace unit with the given name does not
     *                                                 exist.
     */
    public String getUnitDirectory(String key) throws WorkspaceUnitDirectoryNotFoundException {
        Path unitDirectoryPath = Path.of(properties.getWorkspaceDirectory(), key);

        if (Files.notExists(unitDirectoryPath)) {
            throw new WorkspaceUnitDirectoryNotFoundException();
        }

        return unitDirectoryPath.toString();
    }

    /**
     * Writes given content repository to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param name given content repository name.
     * @param input given content repository input.
     * @throws ContentFileWriteFailureException if content file cannot be created.
     */
    public void createContentFile(String workspaceUnitDirectory, String name, InputStream input) throws
            ContentFileWriteFailureException {
        Path contentDirectoryPath = Path.of(workspaceUnitDirectory, properties.getWorkspaceContentDirectory(), name);

        File file = new File(contentDirectoryPath.toString());

        try {
            FileUtils.copyInputStreamToFile(input, file);
        } catch (IOException e) {
            throw new ContentFileWriteFailureException(e.getMessage());
        }
    }

    /**
     * Removes content file in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param name given content repository name.
     * @throws ContentFileRemovalFailureException if content file cannot be created.
     */
    public void removeContentFile(String workspaceUnitDirectory, String name) throws
            ContentFileRemovalFailureException {
        try {
            FileSystemUtils.deleteRecursively(
                    Path.of(workspaceUnitDirectory, properties.getWorkspaceContentDirectory(), name));
        } catch (IOException e) {
            throw new ContentFileRemovalFailureException(e);
        }
    }

    /**
     * Retrieves content file of the given name with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param name                   given name of the content file.
     * @return content file entity.
     * @throws ContentFileNotFoundException if the content file not found.
     */
    public OutputStream getContentFile(String workspaceUnitDirectory, String name) throws
            ContentFileNotFoundException {
        Path contentDirectoryPath = Path.of(workspaceUnitDirectory, properties.getWorkspaceContentDirectory(), name);

        File file = new File(contentDirectoryPath.toString());

        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new ContentFileNotFoundException(e);
        }
    }

    /**
     * Writes metadata file input of the given type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param type                   given type of the metadata file.
     * @param input                  given metadata file entity input.
     * @throws MetadataFileWriteFailureException if metadata file cannot be created.
     */
    private void createMetadataFile(String workspaceUnitDirectory, String type, MetadataFileEntity input)
            throws MetadataFileWriteFailureException {
        ObjectMapper mapper = new ObjectMapper();

        File variableFile =
                new File(
                        Paths.get(workspaceUnitDirectory, properties.getWorkspaceMetadataDirectory(), type)
                                .toString());

        try {
            mapper.writeValue(variableFile, input);
        } catch (IOException e) {
            throw new MetadataFileWriteFailureException(e.getMessage());
        }
    }

    /**
     * Writes metadata file input of the prs type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param input                  given metadata file entity input.
     * @throws MetadataFileWriteFailureException if metadata file cannot be created.
     */
    public void createPRsMetadataFile(String workspaceUnitDirectory, MetadataFileEntity input)
            throws MetadataFileWriteFailureException {
        createMetadataFile(workspaceUnitDirectory, properties.getWorkspacePRsMetadataFileName(), input);
    }

    /**
     * Writes metadata file input of the issues type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param input                  given metadata file entity input.
     * @throws MetadataFileWriteFailureException if metadata file cannot be created.
     */
    public void createIssuesMetadataFile(String workspaceUnitDirectory, MetadataFileEntity input)
            throws MetadataFileWriteFailureException {
        createMetadataFile(workspaceUnitDirectory, properties.getWorkspaceIssuesMetadataFileName(), input);
    }

    /**
     * Writes metadata file input of the releases type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param input                  given metadata file entity input.
     * @throws MetadataFileWriteFailureException if metadata file cannot be created.
     */
    public void createReleasesMetadataFile(String workspaceUnitDirectory, MetadataFileEntity input)
            throws MetadataFileWriteFailureException {
        createMetadataFile(workspaceUnitDirectory, properties.getWorkspaceReleasesMetadataFileName(), input);
    }

    /**
     * Checks if metadata file of the given type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param type                   given type of the metadata file.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isMetadataFileExist(String workspaceUnitDirectory, String type) {
        return Files.exists(
                Paths.get(workspaceUnitDirectory, properties.getWorkspaceMetadataDirectory(), type));
    }

    /**
     * Checks if metadata file of prs type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isPRsMetadataFileExist(String workspaceUnitDirectory) {
        return isMetadataFileExist(workspaceUnitDirectory, properties.getWorkspacePRsMetadataFileName());
    }

    /**
     * Checks if metadata file of issues type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isIssuesMetadataFileExist(String workspaceUnitDirectory) {
        return isMetadataFileExist(workspaceUnitDirectory, properties.getWorkspaceIssuesMetadataFileName());
    }

    /**
     * Checks if metadata file of releases type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isReleasesMetadataFileExist(String workspaceUnitDirectory) {
        return isMetadataFileExist(workspaceUnitDirectory, properties.getWorkspaceReleasesMetadataFileName());
    }

    /**
     * Retrieves metadata file content of the given type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param type                   given type of the metadata file.
     * @return metadata file entity.
     * @throws MetadataFileNotFoundException if the metadata file not found.
     */
    public MetadataFileEntity getMetadataFileContent(String workspaceUnitDirectory, String type)
            throws MetadataFileNotFoundException {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectReader reader = mapper.reader().forType(new TypeReference<MetadataFileEntity>() {
        });

        InputStream variableFile;
        try {
            variableFile =
                    new FileInputStream(
                            Paths.get(workspaceUnitDirectory, properties.getWorkspaceMetadataDirectory(), type)
                                    .toString());
        } catch (FileNotFoundException e) {
            throw new MetadataFileNotFoundException(e.getMessage());
        }

        try {
            return reader.<MetadataFileEntity>readValues(variableFile).readAll().getFirst();
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }

        return null;
    }

    /**
     * Retrieves metadata file content of prs type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return metadata file entity.
     * @throws MetadataFileNotFoundException if the metadata variable file not found.
     */
    public MetadataFileEntity getPRsMetadataFileContent(String workspaceUnitDirectory)
            throws MetadataFileNotFoundException {
        return getMetadataFileContent(workspaceUnitDirectory, properties.getWorkspacePRsMetadataFileName());
    }

    /**
     * Retrieves metadata file content of issues type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return metadata file entity.
     * @throws MetadataFileNotFoundException if the metadata variable file not found.
     */
    public MetadataFileEntity getIssuesMetadataFileContent(String workspaceUnitDirectory)
            throws MetadataFileNotFoundException {
        return getMetadataFileContent(workspaceUnitDirectory, properties.getWorkspaceIssuesMetadataFileName());
    }

    /**
     * Retrieves metadata file content of releases type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return metadata file entity.
     * @throws MetadataFileNotFoundException if the metadata variable file not found.
     */
    public MetadataFileEntity getReleasesMetadataFileContent(String workspaceUnitDirectory)
            throws MetadataFileNotFoundException {
        return getMetadataFileContent(workspaceUnitDirectory, properties.getWorkspaceReleasesMetadataFileName());
    }
}
