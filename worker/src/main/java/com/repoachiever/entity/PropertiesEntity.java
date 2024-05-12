package com.repoachiever.entity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/** Exposes access to properties setup to be used for further configuration. */
@Getter
@Configuration
public class PropertiesEntity {
  @Value("${RESOURCETRACKER_AGENT_CONTEXT}")
  private String agentContext;

  @Value("${kafka.bootstrap-server}")
  private String kafkaBootstrapServer;

  @Value("${kafka.topic}")
  private String kafkaTopic;
}
