package com.repoachiever.service.healthcheck.health;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.*;

/** Service used to check if the application is health. */
@Liveness
@ApplicationScoped
public class HealthCheckService implements HealthCheck {
  @Override
  public HealthCheckResponse call() {
    HealthCheckResponseBuilder healthCheckResponse =
        HealthCheckResponse.named("Docker availability");

    healthCheckResponse.up();

    return healthCheckResponse.build();
  }
}
