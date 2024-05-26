package com.repoachiever.service.healthcheck.readiness;

import com.repoachiever.entity.common.PropertiesEntity;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;

/** Service used to check if the application is ready. */
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
