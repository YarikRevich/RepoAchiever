package com.repoachiever.service.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ConfigValidationException;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service for processing configuration file.
 */
@Startup
@ApplicationScoped
public class ConfigService {
  private static final Logger logger = LogManager.getLogger(ConfigService.class);

  @Inject PropertiesEntity properties;

  private ConfigEntity parsedConfigFile;

  /**
   * Reads configuration from the opened configuration file using mapping with a configuration entity.
   *
   * @throws ConfigValidationException if configuration validation fails.
   */
  @PostConstruct
  private void configure() throws ConfigValidationException {
    InputStream configFile = null;

    try {
      try {
        configFile = new FileInputStream(Paths.get(properties.getConfigDirectory()).toString());
      } catch (FileNotFoundException e) {
        logger.fatal(e.getMessage());
        Quarkus.asyncExit(1);
        return;
      }

      ObjectMapper mapper =
              new ObjectMapper(new YAMLFactory())
                      .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
      mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      ObjectReader reader = mapper.reader().forType(new TypeReference<ConfigEntity>() {
      });

      try {
        List<ConfigEntity> values = reader.<ConfigEntity>readValues(configFile).readAll();
        if (!values.isEmpty()) {
          parsedConfigFile = values.getFirst();
        } else {
          return;
        }
      } catch (IOException e) {
        logger.fatal(e.getMessage());
        Quarkus.asyncExit(1);
        return;
      }

      try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<ConfigEntity>> validationResult =
                validator.validate(parsedConfigFile);

        if (!validationResult.isEmpty()) {
          throw new ConfigValidationException(
                  validationResult.stream()
                          .map(ConstraintViolation::getMessage)
                          .collect(Collectors.joining(", ")));
        }
      }
    } finally {
      try {
        configFile.close();
      } catch (IOException e) {
        logger.fatal(e.getMessage());
        Quarkus.asyncExit(1);
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
