package com.repoachiever.entity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/** Exposes access to properties setup to be used for further configuration. */
@Getter
@Configuration
public class PropertiesEntity {
  @Value("${REPOACHIEVER_CLUSTER_CONTEXT:null}")
  private String clusterContext;
}
