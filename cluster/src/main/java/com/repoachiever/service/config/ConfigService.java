package com.repoachiever.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ConfigNotGivenException;
import com.repoachiever.exception.ConfigValidationException;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service used to perform RepoAchiever Cluster configuration processing operation.
 */
@Component
public class ConfigService {
    private static final Logger logger = LogManager.getLogger(ConfigService.class);

    @Autowired
    private PropertiesEntity properties;

    private ConfigEntity parsedConfigFile;

    /**
     * Performs configuration file parsing operation.
     */
    @PostConstruct
    private void configure() {
        String clusterContext = properties.getClusterContext();

        if (Objects.equals(clusterContext, "null")) {
            logger.fatal(new ConfigNotGivenException().getMessage());
            return;
        }

        InputStream configFile = null;

        try {
            configFile = IOUtils.toInputStream(clusterContext, "UTF-8");

            ObjectMapper mapper =
                    new ObjectMapper(new JsonFactory())
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
                return;
            }

            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = validatorFactory.getValidator();

                Set<ConstraintViolation<ConfigEntity>> validationResult =
                        validator.validate(parsedConfigFile);

                if (!validationResult.isEmpty()) {
                    logger.fatal(new ConfigValidationException(
                            validationResult.stream()
                                    .map(ConstraintViolation::getMessage)
                                    .collect(Collectors.joining(", "))).getMessage());
                }
            }
        } finally {
            try {
                configFile.close();
            } catch (IOException e) {
                logger.fatal(e.getMessage());
            }
        }
    }

    /**
     * Retrieves parsed configuration file entity.
     *
     * @return retrieved parsed configuration file entity.
     */
    public ConfigEntity getConfig() {
        return parsedConfigFile;
    }
}
