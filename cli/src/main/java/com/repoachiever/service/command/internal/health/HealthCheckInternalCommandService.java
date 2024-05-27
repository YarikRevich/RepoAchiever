package com.repoachiever.service.command.internal.health;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.HealthCheckStatus;
import com.repoachiever.service.client.health.HealthClientService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents health internal command service.
 */
@Service
public class HealthCheckInternalCommandService implements ICommand<ConfigEntity> {
  @Autowired private VisualizationState visualizationState;

  /**
   * @see ICommand
   */
  @Override
  public void process(ConfigEntity config) throws ApiServerOperationFailureException {
    visualizationState.getLabel().pushNext();

    HealthClientService healthClientService =
            new HealthClientService(config.getApiServer().getHost());

    HealthCheckResult healthCheckResult = healthClientService.process(null);

    if (healthCheckResult.getStatus() == HealthCheckStatus.DOWN) {
      throw new ApiServerOperationFailureException(
          new ApiServerNotAvailableException(healthCheckResult.getChecks().toString())
              .getMessage());
    }

    visualizationState.getLabel().pushNext();
  }
}
