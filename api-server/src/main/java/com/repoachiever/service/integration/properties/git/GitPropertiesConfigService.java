package com.repoachiever.service.integration.properties.git;

import io.quarkus.runtime.annotations.StaticInitSafe;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.microprofile.config.spi.ConfigSource;

/** Provides access to external config source used as a source of Git info. */
@StaticInitSafe
public class GitPropertiesConfigService implements ConfigSource {
  private static final Logger logger = LogManager.getLogger(GitPropertiesConfigService.class);

  private static final String GIT_CONFIG_PROPERTIES_FILE = "git.properties";

  private final Properties config;

  public GitPropertiesConfigService() {
    this.config = new Properties();

    ClassLoader classLoader = getClass().getClassLoader();
    InputStream gitBuildPropertiesStream =
        classLoader.getResourceAsStream(GIT_CONFIG_PROPERTIES_FILE);
    try {
      config.load(gitBuildPropertiesStream);
    } catch (IOException e) {
      logger.fatal(e.getMessage());
    }
  }

  /**
   * @see ConfigSource
   */
  @Override
  public Map<String, String> getProperties() {
    return ConfigSource.super.getProperties();
  }

  /**
   * @see ConfigSource
   */
  @Override
  public Set<String> getPropertyNames() {
    return config.keySet().stream().map(element -> (String) element).collect(Collectors.toSet());
  }

  /**
   * @see ConfigSource
   */
  @Override
  public int getOrdinal() {
    return ConfigSource.super.getOrdinal();
  }

  /**
   * @see ConfigSource
   */
  @Override
  public String getValue(String key) {
    return (String) config.get(key);
  }

  /**
   * @see ConfigSource
   */
  @Override
  public String getName() {
    return GitPropertiesConfigService.class.getSimpleName();
  }
}
