package com.repoachiever.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.io.*;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for processing configuration file.
 */
@Service
public class ConfigService {
    private static final Logger logger = LogManager.getLogger(ConfigService.class);

    private InputStream configFile;

    private ConfigEntity parsedConfigFile;

    /**
     * Default constructor, which opens configuration file at the given path.
     *
     * @param properties common application properties
     */
    public ConfigService(@Autowired PropertiesEntity properties) {
        try {
            configFile =
                    new FileInputStream(
                            Paths.get(
                                            System.getProperty("user.home"),
                                            properties.getConfigRootPath(),
                                            properties.getConfigUserFilePath())
                                    .toString());
        } catch (FileNotFoundException e) {
            logger.fatal(e.getMessage());
        }
    }

    /**
     * Reads configuration from the opened configuration file using mapping with a configuration
     * entity.
     */
    @PostConstruct
    private void configure() {
        ObjectMapper mapper =
                new ObjectMapper(new YAMLFactory())
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectReader reader = mapper.reader().forType(new TypeReference<ConfigEntity>() {
        });

        try {
            parsedConfigFile = reader.<ConfigEntity>readValues(configFile).readAll().getFirst();
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }
    }

    /**
     * @return Parsed configuration entity
     */
    public ConfigEntity getConfig() {
        return parsedConfigFile;
    }

    @PreDestroy
    private void close() {
        try {
            configFile.close();
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }
    }
}
