package com.repoachiever.entity;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/** Represents application properties used for application configuration. */
@Getter
@Configuration
public class PropertiesEntity {
  private static final String GIT_CONFIG_PROPERTIES_FILE = "git.properties";

  @Value(value = "${git.commit.id.abbrev}")
  private String gitCommitId;

  @Value(value = "${config.default.directory}")
  private String configDefaultDirectory;

  @Value(value = "${config.default.name}")
  private String configDefaultName;

  @Value(value = "${progress.visualization.period}")
  private Integer progressVisualizationPeriod;

  @Value(value = "${progress.visualization.secrets-acquire-request}")
  private String progressVisualizationSecretsAcquireRequestLabel;

  @Value(value = "${progress.visualization.script-acquire-request}")
  private String progressVisualizationScriptAcquireRequestLabel;

  @Value(value = "${progress.visualization.apply-request}")
  private String progressVisualizationApplyRequestLabel;

  @Value(value = "${progress.visualization.apply-response}")
  private String progressVisualizationApplyResponseLabel;

  @Value(value = "${progress.visualization.destroy-request}")
  private String progressVisualizationDestroyRequestLabel;

  @Value(value = "${progress.visualization.destroy-response}")
  private String progressVisualizationDestroyResponseLabel;

  @Value(value = "${progress.visualization.state-request}")
  private String progressVisualizationStateRequestLabel;

  @Value(value = "${progress.visualization.state-response}")
  private String progressVisualizationStateResponseLabel;

  @Value(value = "${progress.visualization.version-info-request}")
  private String progressVisualizationVersionInfoRequestLabel;

  @Value(value = "${progress.visualization.health-check-request}")
  private String progressVisualizationHealthCheckRequestLabel;

  @Value(value = "${progress.visualization.readiness-check-request}")
  private String progressVisualizationReadinessCheckRequestLabel;

  @Value(value = "${logging.state.frequency}")
  private Integer loggingStateFrequency;

  @Bean
  private static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocation(new ClassPathResource(GIT_CONFIG_PROPERTIES_FILE));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }

  /**
   * Removes the last symbol in git commit id of the repository.
   *
   * @return chopped repository git commit id.
   */
  public String getGitCommitId() {
    return StringUtils.chop(gitCommitId);
  }
}
