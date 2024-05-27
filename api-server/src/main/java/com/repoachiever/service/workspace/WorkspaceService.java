package com.repoachiever.service.workspace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.entity.common.AdditionalContentFileEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.xml.bind.DatatypeConverter;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.util.FileSystemUtils;

/**
 * Represents local content workspace for different users.
 */
@ApplicationScoped
public class WorkspaceService {
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
     * Creates content unit directory in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given content location.
     * @throws WorkspaceContentDirectoryCreationFailureException if workspace content unit directory creation
     *                                                           operation failed.
     */
    public void createContentDirectory(String workspaceUnitDirectory, String location) throws
            WorkspaceContentDirectoryCreationFailureException {
        Path unitDirectoryPath = Path.of(workspaceUnitDirectory, location);

        if (Files.notExists(unitDirectoryPath)) {
            try {
                Files.createDirectory(unitDirectoryPath);
            } catch (IOException e) {
                throw new WorkspaceContentDirectoryCreationFailureException(e.getMessage());
            }
        }
    }

    /**
     * Creates raw content directory in the given workspace unit directory and location.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given raw content location.
     * @throws WorkspaceContentDirectoryCreationFailureException if workspace raw content directory creation operation
     *                                                           failed.
     */
    public void createRawContentDirectory(String workspaceUnitDirectory, String location) throws
            WorkspaceContentDirectoryCreationFailureException {
        Path unitDirectoryPath = Path.of(
                workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory());

        if (Files.notExists(unitDirectoryPath)) {
            try {
                Files.createDirectory(unitDirectoryPath);
            } catch (IOException e) {
                throw new WorkspaceContentDirectoryCreationFailureException(e.getMessage());
            }
        }
    }

    /**
     * Creates additional content directory in the given workspace unit directory and location.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given additional content location.
     * @throws WorkspaceContentDirectoryCreationFailureException if workspace additional content directory creation
     *                                                           operation failed.
     */
    public void createAdditionalContentDirectory(String workspaceUnitDirectory, String location) throws
            WorkspaceContentDirectoryCreationFailureException {
        Path unitDirectoryPath = Path.of(
                workspaceUnitDirectory, location, properties.getWorkspaceAdditionalContentDirectory());

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
     * Removes content directory in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given content directory location.
     * @throws ContentDirectoryRemovalFailureException if content directory cannot be removed.
     */
    public void removeContentDirectory(String workspaceUnitDirectory, String location) throws
            ContentDirectoryRemovalFailureException {
        try {
            FileSystemUtils.deleteRecursively(Path.of(workspaceUnitDirectory, location));
        } catch (IOException e) {
            throw new ContentDirectoryRemovalFailureException(e.getMessage());
        }
    }

    /**
     * Checks if workspace unit directory exists with the help of the given key.
     *
     * @param key given workspace unit directory.
     * @return result if workspace unit directory exists with the help of the given key.
     */
    public Boolean isUnitDirectoryExist(String key) {
        return Files.exists(Paths.get(properties.getWorkspaceDirectory(), key));
    }

    /**
     * Checks if content unit exists with the help of the given key and location.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given content location.
     * @return result if content directory exists with the help of the given key and location.
     */
    public Boolean isContentDirectoryExist(String workspaceUnitDirectory, String location) {
        return Files.exists(Paths.get(workspaceUnitDirectory, location));
    }

    /**
     * Checks if raw content directory exists with the help of the given key and location.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given raw content location.
     * @return result if raw content directory exists with the help of the given key and location.
     */
    public Boolean isRawContentDirectoryExist(String workspaceUnitDirectory, String location) {
        return Files.exists(
                Paths.get(workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory()));
    }

    /**
     * Checks if additional content directory exists with the help of the given key and location.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given additional content location.
     * @return result if additional content directory exists with the help of the given key and location.
     */
    public Boolean isAdditionalContentDirectoryExist(String workspaceUnitDirectory, String location) {
        return Files.exists(
                Paths.get(workspaceUnitDirectory, location, properties.getWorkspaceAdditionalContentDirectory()));
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
     * Retrieves amount of files of the given type in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given raw content file location.
     * @param type                   given file type.
     * @throws ContentFilesAmountRetrievalFailureException if files amount retrieval failed.
     */
    private Integer getFilesAmount(String workspaceUnitDirectory, String location, String type) throws
            ContentFilesAmountRetrievalFailureException {
        try (Stream<Path> stream = Files.list(Path.of(workspaceUnitDirectory, location, type))) {
            return (int) stream.count();
        } catch (IOException e) {
            throw new ContentFilesAmountRetrievalFailureException(e.getMessage());
        }
    }

    /**
     * Removes earliest file of the given type in the given workspace unit according to the creation timestamp.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given file location.
     * @param type                   given file type.
     * @throws ContentFileRemovalFailureException if earliest file removal operation failed. .
     */
    private void removeEarliestFile(String workspaceUnitDirectory, String location, String type) throws
            ContentFileRemovalFailureException {
        Path target = null;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(workspaceUnitDirectory, location, type))) {
            Instant earliestTimestamp = null;

            for (Path file : stream) {
                BasicFileAttributes attributes;

                try {
                    attributes = Files.readAttributes(file, BasicFileAttributes.class);
                } catch (IOException e) {
                    throw new RawContentFileRemovalFailureException(e.getMessage());
                }

                if (Objects.isNull(earliestTimestamp) ||
                        attributes.creationTime().toInstant().isBefore(earliestTimestamp)) {
                    earliestTimestamp = attributes.creationTime().toInstant();
                    target = file;
                }
            }
        } catch (IOException e) {
            throw new ContentFileRemovalFailureException(e.getMessage());
        }

        if (Objects.nonNull(target)) {
            try {
                FileSystemUtils.deleteRecursively(target);
            } catch (IOException e) {
                throw new ContentFileRemovalFailureException(e.getMessage());
            }
        }
    }

    /**
     * Retrieves content units locations in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @return a list of content units locations.
     * @throws ContentUnitsLocationsRetrievalFailureException if content units locations retrieval operation failed. .
     */
    public List<String> getContentUnitsLocations(String workspaceUnitDirectory) throws
            ContentUnitsLocationsRetrievalFailureException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(workspaceUnitDirectory))) {
            List<String> result = new ArrayList<>();

            for (Path file : stream) {
                result.add(file.getFileName().toString());
            }

            return result;
        } catch (IOException e) {
            throw new ContentUnitsLocationsRetrievalFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves content files locations of the given type in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given file location.
     * @param type                   given file type.
     * @return a list of content locations.
     * @throws ContentFilesLocationsRetrievalFailureException if content files locations retrieval operation failed. .
     */
    public List<String> getContentFilesLocations(String workspaceUnitDirectory, String location, String type) throws
            ContentFilesLocationsRetrievalFailureException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(workspaceUnitDirectory, location, type))) {
            List<String> result = new ArrayList<>();

            for (Path file : stream) {
                result.add(file.getFileName().toString());
            }

            return result;
        } catch (IOException e) {
            throw new ContentFilesLocationsRetrievalFailureException(e.getMessage());
        }
    }

    /**
     * Writes given raw content to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given raw content location.
     * @param name                   given raw content name.
     * @param input                  given raw content input.
     * @throws RawContentFileWriteFailureException if raw content file cannot be created.
     */
    public void createRawContentFile(
            String workspaceUnitDirectory, String location, String name, InputStream input) throws
            RawContentFileWriteFailureException {
        Path contentDirectoryPath = Path.of(
                workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory(), name);

        File file = new File(contentDirectoryPath.toString());

        try {
            FileUtils.copyInputStreamToFile(input, file);
        } catch (IOException e) {
            throw new RawContentFileWriteFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves amount of raw content files in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given raw content file location.
     * @throws RawContentFilesAmountRetrievalFailureException if raw content files amount retrieval failed.
     */
    public Integer getRawContentFilesAmount(String workspaceUnitDirectory, String location) throws
            RawContentFilesAmountRetrievalFailureException {
        try {
            return getFilesAmount(workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory());
        } catch (ContentFilesAmountRetrievalFailureException e) {
            throw new RawContentFilesAmountRetrievalFailureException(e.getMessage());
        }
    }

    /**
     * Removes earliest raw content file in the given workspace unit according to the creation timestamp.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given raw content file location.
     * @throws RawContentFileRemovalFailureException if earliest raw content file removal operation failed. .
     */
    public void removeEarliestRawContentFile(String workspaceUnitDirectory, String location) throws
            RawContentFileRemovalFailureException {
        try {
            removeEarliestFile(workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory());
        } catch (ContentFileRemovalFailureException e) {
            throw new RawContentFileRemovalFailureException(e.getMessage());
        }
    }

    /**
     * Checks if additional content file of the given type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given location of the additional content file.
     * @param name                   given name of the additional content file.
     * @return result if additional content file exists in the given workspace unit directory.
     */
    public Boolean isRawContentFileExist(String workspaceUnitDirectory, String location, String name) {
        return Files.exists(
                Paths.get(
                        workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory(), name));
    }

    /**
     * Retrieves content files locations of raw type in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given file location.
     * @return a list of raw content locations.
     * @throws ContentFilesLocationsRetrievalFailureException if files locations retrieval operation failed. .
     */
    public List<String> getRawContentFilesLocations(String workspaceUnitDirectory, String location) throws
            ContentFilesLocationsRetrievalFailureException {
        return getContentFilesLocations(workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory());
    }

    /**
     * Retrieves raw content file of the given name with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given location of the content file.
     * @param name                   given name of the content file.
     * @return raw content file stream.
     * @throws ContentFileNotFoundException if the raw content file not found.
     */
    public byte[] getRawContentFile(String workspaceUnitDirectory, String location, String name) throws
            ContentFileNotFoundException {
        Path contentDirectoryPath = Path.of(
                workspaceUnitDirectory, location, properties.getWorkspaceRawContentDirectory(), name);

        try {
            return FileUtils.readFileToByteArray(new File(contentDirectoryPath.toString()));
        } catch (IOException e) {
            throw new ContentFileNotFoundException(e.getMessage());
        }
    }

    /**
     * Writes additional file input of the given type to the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given location of the additional content file.
     * @param name                   given name of the additional content file.
     * @param input                  given additional content file entity input.
     * @throws AdditionalContentFileWriteFailureException if additional content file cannot be created.
     */
    public void createAdditionalContentFile(
            String workspaceUnitDirectory, String location, String name, AdditionalContentFileEntity input)
            throws AdditionalContentFileWriteFailureException {
        ObjectMapper mapper = new ObjectMapper();

        File variableFile =
                new File(
                        Paths.get(
                                workspaceUnitDirectory,
                                location,
                                properties.getWorkspaceAdditionalContentDirectory(),
                                name).toString());

        try {
            mapper.writeValue(variableFile, input);
        } catch (IOException e) {
            throw new AdditionalContentFileWriteFailureException(e.getMessage());
        }
    }

    /**
     * Retrieves amount of additional content files in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given additional content file location.
     * @throws AdditionalContentFilesAmountRetrievalFailureException if additional content files amount retrieval failed.
     */
    public Integer getAdditionalContentFilesAmount(String workspaceUnitDirectory, String location) throws
            AdditionalContentFilesAmountRetrievalFailureException {
        try {
            return getFilesAmount(workspaceUnitDirectory, location, properties.getWorkspaceAdditionalContentDirectory());
        } catch (ContentFilesAmountRetrievalFailureException e) {
            throw new AdditionalContentFilesAmountRetrievalFailureException(e.getMessage());
        }
    }

    /**
     * Checks if additional content file of the given type exists in the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given location of the additional content file.
     * @param name                   given name of the additional content file.
     * @return result if additional content file exists in the given workspace unit directory.
     */
    public Boolean isAdditionalContentFileExist(String workspaceUnitDirectory, String location, String name) {
        return Files.exists(
                Paths.get(
                        workspaceUnitDirectory, location, properties.getWorkspaceAdditionalContentDirectory(), name));
    }

    /**
     * Retrieves content files locations of additional type in the given workspace unit.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given file location.
     * @return a list of additional content locations.
     * @throws ContentFilesLocationsRetrievalFailureException if files locations retrieval operation failed. .
     */
    public List<String> getAdditionalContentFilesLocations(String workspaceUnitDirectory, String location) throws
            ContentFilesLocationsRetrievalFailureException {
        return getContentFilesLocations(
                workspaceUnitDirectory, location, properties.getWorkspaceAdditionalContentDirectory());
    }

    /**
     * Retrieves additional content file content of the given name with the help of the given workspace unit directory.
     *
     * @param workspaceUnitDirectory given workspace unit directory.
     * @param location               given location of the additional content file.
     * @param name                   given name of the additional content file.
     * @return additional content file entity.
     * @throws AdditionalContentFileNotFoundException if the additional content file not found.
     */
    public AdditionalContentFileEntity getAdditionalContentFileContent(
            String workspaceUnitDirectory, String location, String name) throws
            AdditionalContentFileNotFoundException, AdditionalContentFileReadFailureException {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectReader reader = mapper.reader().forType(new TypeReference<AdditionalContentFileEntity>() {
        });

        InputStream variableFile;
        try {
            variableFile =
                    new FileInputStream(
                            Paths.get(
                                            workspaceUnitDirectory,
                                            location,
                                            properties.getWorkspaceAdditionalContentDirectory(),
                                            name)
                                    .toString());
        } catch (FileNotFoundException e) {
            throw new AdditionalContentFileNotFoundException(e.getMessage());
        }

        try {
            return reader.<AdditionalContentFileEntity>readValues(variableFile).readAll().getFirst();
        } catch (IOException e) {
            throw new AdditionalContentFileReadFailureException(e.getMessage());
        }
    }
}
