package com.repoachiever.service.command.internal.health;

import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.HealthCheckStatus;
import com.repoachiever.service.client.command.HealthCheckClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckInternalCommandService implements ICommand {
  @Autowired private HealthCheckClientCommandService healthCheckClientCommandService;

  @Autowired private VisualizationState visualizationState;

  /**
   * @see ICommand
   */
  @Override
  public void process() throws ApiServerException {
    visualizationState.getLabel().pushNext();

    HealthCheckResult healthCheckResult = healthCheckClientCommandService.process(null);

    if (healthCheckResult.getStatus() == HealthCheckStatus.DOWN) {
      throw new ApiServerException(
          new ApiServerNotAvailableException(healthCheckResult.getChecks().toString())
              .getMessage());
    }
  }
}
