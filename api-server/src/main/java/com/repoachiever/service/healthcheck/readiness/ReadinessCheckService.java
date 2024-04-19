package com.repoachiever.service.healthcheck.readiness;

import com.repoachiever.entity.PropertiesEntity;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

/** Checks if the Kafka service is available for the given workspace. */
public class ReadinessCheckService implements HealthCheck {

  public ReadinessCheckService(PropertiesEntity properties) {
  }

  @Override
  public HealthCheckResponse call() {
    HealthCheckResponseBuilder healthCheckResponse =
        HealthCheckResponse.named("Connection availability");

    healthCheckResponse.up();

    return healthCheckResponse.build();
  }
}
