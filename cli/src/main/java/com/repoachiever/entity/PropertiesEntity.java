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

  @Value(value = "${config.default.location}")
  private String configDefaultLocation;

  @Value(value = "${progress.visualization.period}")
  private Integer progressVisualizationPeriod;

  @Value(value = "${progress.visualization.apply-request}")
  private String progressVisualizationApplyRequestLabel;

  @Value(value = "${progress.visualization.apply-response}")
  private String progressVisualizationApplyResponseLabel;

  @Value(value = "${progress.visualization.withdraw-request}")
  private String progressVisualizationWithdrawRequestLabel;

  @Value(value = "${progress.visualization.withdraw-response}")
  private String progressVisualizationWithdrawResponseLabel;

  @Value(value = "${progress.visualization.clean-request}")
  private String progressVisualizationCleanRequestLabel;

  @Value(value = "${progress.visualization.clean-response}")
  private String progressVisualizationCleanResponseLabel;

  @Value(value = "${progress.visualization.clean-all-request}")
  private String progressVisualizationCleanAllRequestLabel;

  @Value(value = "${progress.visualization.clean-all-response}")
  private String progressVisualizationCleanAllResponseLabel;

  @Value(value = "${progress.visualization.content-request}")
  private String progressVisualizationContentRequestLabel;

  @Value(value = "${progress.visualization.content-response}")
  private String progressVisualizationContentResponseLabel;

  @Value(value = "${progress.visualization.download-request}")
  private String progressVisualizationDownloadRequestLabel;

  @Value(value = "${progress.visualization.download-response}")
  private String progressVisualizationDownloadResponseLabel;

  @Value(value = "${progress.visualization.topology-request}")
  private String progressVisualizationTopologyRequestLabel;

  @Value(value = "${progress.visualization.topology-response}")
  private String progressVisualizationTopologyResponseLabel;

  @Value(value = "${progress.visualization.version-request}")
  private String progressVisualizationVersionRequestLabel;

  @Value(value = "${progress.visualization.version-response}")
  private String progressVisualizationVersionResponseLabel;

  @Value(value = "${progress.visualization.health-check-request}")
  private String progressVisualizationHealthCheckRequestLabel;

  @Value(value = "${progress.visualization.health-check-response}")
  private String progressVisualizationHealthCheckResponseLabel;

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
