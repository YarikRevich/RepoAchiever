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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents service used for configuration processing. */
@Service
public class ConfigService {
  private static final Logger logger = LogManager.getLogger(ConfigService.class);

  @Autowired private PropertiesEntity properties;

  private ConfigEntity parsedConfigFile;

  /** Processes configuration file */
  @PostConstruct
  public void configure() {
    InputStream configFile;

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
      return;
    }

    ObjectMapper mapper =
        new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    ObjectReader reader = mapper.reader().forType(new TypeReference<ConfigEntity>() {});

    try {
      parsedConfigFile = reader.<ConfigEntity>readValues(configFile).readAll().getFirst();
    } catch (IOException e) {
      logger.fatal(e.getMessage());
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
