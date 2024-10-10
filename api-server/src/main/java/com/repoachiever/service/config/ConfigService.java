package com.repoachiever.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.repoachiever.entity.common.ConfigEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ConfigCronExpressionValidationException;
import com.repoachiever.exception.ConfigFileClosureFailureException;
import com.repoachiever.exception.ConfigFileNotFoundException;
import com.repoachiever.exception.ConfigFileReadingFailureException;
import com.repoachiever.exception.ConfigValidationException;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

import org.apache.logging.log4j.core.util.CronExpression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service used to perform RepoAchiever API Server configuration processing
 * operation.
 */
@Startup
@ApplicationScoped
public class ConfigService {
    @Inject
    PropertiesEntity properties;

    @Getter
    private ConfigEntity config;

    /**
     * Reads configuration from the opened configuration file using mapping with a
     * configuration entity.
     *
     * @throws ConfigFileNotFoundException       if configuration file is not found.
     * @throws ConfigValidationException         if configuration file operation
     *                                           failed.
     * @throws ConfigFileReadingFailureException if configuration file reading
     *                                           operation failed.
     * @throws ConfigFileClosureFailureException if configuration file closure
     *                                           operation failed.
     */
    @PostConstruct
    private void configure() throws ConfigFileNotFoundException,
            ConfigValidationException,
            ConfigFileReadingFailureException,
            ConfigFileClosureFailureException {
        InputStream file = null;

        try {
            try {
                file = new FileInputStream(Paths.get(properties.getConfigLocation()).toString());
            } catch (FileNotFoundException e) {
                throw new ConfigFileNotFoundException(e.getMessage());
            }

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
                    .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                    .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            ObjectReader reader = mapper.reader().forType(new TypeReference<ConfigEntity>() {
            });

            try {
                List<ConfigEntity> values = reader.<ConfigEntity>readValues(file).readAll();
                if (values.isEmpty()) {
                    return;
                }

                config = values.getFirst();
            } catch (IOException e) {
                throw new ConfigFileReadingFailureException(e.getMessage());
            }

            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = validatorFactory.getValidator();

                Set<ConstraintViolation<ConfigEntity>> validationResult = validator.validate(config);

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
                file.close();
            } catch (IOException e) {
                throw new ConfigFileClosureFailureException(e.getMessage());
            }
        }
    }
}
