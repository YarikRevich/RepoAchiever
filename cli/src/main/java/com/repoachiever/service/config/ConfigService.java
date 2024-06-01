package com.repoachiever.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ConfigFileClosureFailureException;
import com.repoachiever.exception.ConfigFileNotFoundException;
import com.repoachiever.exception.ConfigFileReadingFailureException;
import com.repoachiever.exception.ConfigValidationException;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service used to perform RepoAchiever CLI configuration processing operation.
 */
@Getter
@Component
public class ConfigService {
    private ConfigEntity config;

    /**
     * Performs configuration file parsing operation.
     *
     * @param configLocation given configuration file location.
     * @throws ConfigFileNotFoundException if configuration file is not found.
     * @throws ConfigValidationException if configuration file operation failed.
     * @throws ConfigFileReadingFailureException if configuration file reading operation failed.
     * @throws ConfigFileClosureFailureException if configuration file closure operation failed.
     */
    public void configure(String configLocation) throws
            ConfigFileNotFoundException,
            ConfigFileReadingFailureException,
            ConfigValidationException,
            ConfigFileClosureFailureException {
        InputStream file;

        try {
            file = new FileInputStream(Paths.get(configLocation).toString());
        } catch (FileNotFoundException e) {
            throw new ConfigFileNotFoundException(e.getMessage());
        }

        try {
            ObjectMapper mapper =
                    new ObjectMapper(new YAMLFactory())
                            .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            ObjectReader reader = mapper.reader().forType(new TypeReference<ConfigEntity>() {
            });

            try {
                config = reader.<ConfigEntity>readValues(file).readAll().getFirst();
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
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                throw new ConfigFileClosureFailureException(e.getMessage());
            }
        }
    }
}
