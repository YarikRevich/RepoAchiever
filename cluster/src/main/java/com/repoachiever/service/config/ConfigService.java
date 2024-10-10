package com.repoachiever.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ConfigFileClosureFailureException;
import com.repoachiever.exception.ConfigFileReadingFailureException;
import com.repoachiever.exception.ConfigNotGivenException;
import com.repoachiever.exception.ConfigValidationException;
import com.repoachiever.exception.ConfigCronExpressionValidationException;
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
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service used to perform RepoAchiever Cluster configuration processing operation.
 */
@Component
public class ConfigService {
    @Autowired
    private PropertiesEntity properties;

    @Getter
    private ConfigEntity config;

    /**
     * Performs configuration file parsing operation.
     *
     * @throws ConfigNotGivenException if configuration file content is not given.
     * @throws ConfigValidationException if configuration file operation failed.
     * @throws ConfigFileReadingFailureException if configuration file reading operation failed.
     * @throws ConfigFileClosureFailureException if configuration file closure operation failed.
     */
    @PostConstruct
    private void configure() throws
            ConfigNotGivenException,
            ConfigFileReadingFailureException,
            ConfigValidationException,
            ConfigFileClosureFailureException {
        String clusterContext = properties.getClusterContext();

        if (Objects.equals(clusterContext, "null")) {
            throw new ConfigNotGivenException();
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
                config = reader.<ConfigEntity>readValues(configFile).readAll().getFirst();
            } catch (IOException e) {
                throw new ConfigFileReadingFailureException(e.getMessage());
            }

            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = validatorFactory.getValidator();

                Set<ConstraintViolation<ConfigEntity>> validationResult =
                        validator.validate(config);

                if (!validationResult.isEmpty()) {
                    throw new ConfigValidationException(
                            validationResult.stream()
                                    .map(ConstraintViolation::getMessage)
                                    .collect(Collectors.joining(", ")));
                }
            }

            if (!CronExpression.isValidExpression(
                    config.getResource().getWorker().getFrequency())) {
                throw new ConfigValidationException(
                        new ConfigCronExpressionValidationException().getMessage());
            }
        } finally {
            try {
                configFile.close();
            } catch (IOException e) {
                throw new ConfigFileClosureFailureException(e.getMessage());
            }
        }
    }
}
