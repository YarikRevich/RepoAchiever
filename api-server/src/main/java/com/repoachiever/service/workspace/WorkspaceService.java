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
     * Creates workspace unit with the help of the given key.
     *
     * @param key given workspace unit key.
     * @throws IOException if IO operation failed.
     */
    public void createUnitDirectory(String key) throws IOException {
        Path unitDirectoryPath = Path.of(properties.getWorkspaceDirectory(), key);

        if (Files.notExists(unitDirectoryPath)) {
            Files.createDirectory(unitDirectoryPath);
        }
    }

    /**
     * Removes workspace unit with the help of the given key.
     *
     * @param key given workspace unit key.
     * @throws IOException if IO operation failed.
     */
    public void removeUnitDirectory(String key) throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(properties.getWorkspaceDirectory(), key));
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
     * Writes metadata file input of the given type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param type                   given type of the metadata file.
     * @param input                  given metadata file entity input.
     * @throws VariableFileWriteFailureException if metadata file cannot be created.
     */
    private void createMetadataFile(String workspaceUnitDirectory, String type, MetadataFileEntity input)
            throws VariableFileWriteFailureException {
        ObjectMapper mapper = new ObjectMapper();

        File variableFile =
                new File(
                        Paths.get(workspaceUnitDirectory, type)
                                .toString());

        try {
            mapper.writeValue(variableFile, input);
        } catch (IOException e) {
            throw new VariableFileWriteFailureException(e.getMessage());
        }
    }

    /**
     * Writes metadata file input of the prs type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param input                  given metadata file entity input.
     * @throws VariableFileWriteFailureException if metadata file cannot be created.
     */
    public void createPRsMetadataFile(String workspaceUnitDirectory, MetadataFileEntity input)
            throws VariableFileWriteFailureException {
        createMetadataFile(workspaceUnitDirectory, properties.getWorkspacePRsMetadataFileName(), input);
    }

    /**
     * Writes metadata file input of the issues type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param input                  given metadata file entity input.
     * @throws VariableFileWriteFailureException if metadata file cannot be created.
     */
    public void createIssuesMetadataFile(String workspaceUnitDirectory, MetadataFileEntity input)
            throws VariableFileWriteFailureException {
        createMetadataFile(workspaceUnitDirectory, properties.getWorkspaceIssuesMetadataFileName(), input);
    }

    /**
     * Writes metadata file input of the releases type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param input                  given metadata file entity input.
     * @throws VariableFileWriteFailureException if metadata file cannot be created.
     */
    public void createReleasesMetadataFile(String workspaceUnitDirectory, MetadataFileEntity input)
            throws VariableFileWriteFailureException {
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
                Paths.get(workspaceUnitDirectory, type));
    }

    /**
     * Checks if metadata file of prs type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isPRsMetadataFileExist(String workspaceUnitDirectory) {
        return Files.exists(
                Paths.get(workspaceUnitDirectory, properties.getWorkspacePRsMetadataFileName()));
    }

    /**
     * Checks if metadata file of issues type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isIssuesMetadataFileExist(String workspaceUnitDirectory) {
        return Files.exists(
                Paths.get(workspaceUnitDirectory, properties.getWorkspaceIssuesMetadataFileName()));
    }

    /**
     * Checks if metadata file of releases type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return result if metadata file exists in the given workspace unit directory.
     */
    private boolean isReleasesMetadataFileExist(String workspaceUnitDirectory) {
        return Files.exists(
                Paths.get(workspaceUnitDirectory, properties.getWorkspaceReleasesMetadataFileName()));
    }

    /**
     * Retrieves metadata file content of the given type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param type given type of the metadata file.
     * @return metadata file entity.
     * @throws VariableFileNotFoundException if the metadata file not found.
     */
    public MetadataFileEntity getMetadataFileContent(String workspaceUnitDirectory, String type)
            throws VariableFileNotFoundException {
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
                            Paths.get(workspaceUnitDirectory, type)
                                    .toString());
        } catch (FileNotFoundException e) {
            throw new VariableFileNotFoundException(e.getMessage());
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
     * @throws VariableFileNotFoundException if the metadata variable file not found.
     */
    public MetadataFileEntity getPRsMetadataFileContent(String workspaceUnitDirectory)
            throws VariableFileNotFoundException {
        return getMetadataFileContent(workspaceUnitDirectory, properties.getWorkspacePRsMetadataFileName());
    }

    /**
     * Retrieves metadata file content of issues type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return metadata file entity.
     * @throws VariableFileNotFoundException if the metadata variable file not found.
     */
    public MetadataFileEntity getIssuesMetadataFileContent(String workspaceUnitDirectory)
            throws VariableFileNotFoundException {
        return getMetadataFileContent(workspaceUnitDirectory, properties.getWorkspaceIssuesMetadataFileName());
    }

    /**
     * Retrieves metadata file content of releases type with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return metadata file entity.
     * @throws VariableFileNotFoundException if the metadata variable file not found.
     */
    public MetadataFileEntity getReleasesMetadataFileContent(String workspaceUnitDirectory)
            throws VariableFileNotFoundException {
        return getMetadataFileContent(workspaceUnitDirectory, properties.getWorkspaceReleasesMetadataFileName());
    }
}
