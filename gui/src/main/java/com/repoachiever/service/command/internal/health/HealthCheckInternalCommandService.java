package com.repoachiever.service.command.internal.health;

import com.repoachiever.dto.HealthCheckInternalCommandResultDto;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.HealthCheckStatus;
import com.repoachiever.service.client.command.HealthCheckClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckInternalCommandService
    implements ICommand<HealthCheckInternalCommandResultDto> {
  @Autowired private HealthCheckClientCommandService healthCheckClientCommandService;

  /**
   * @see ICommand
   */
  @Override
  public HealthCheckInternalCommandResultDto process() {
    HealthCheckResult healthCheckResult;
    try {
      healthCheckResult = healthCheckClientCommandService.process(null);
    } catch (ApiServerException e) {
      return HealthCheckInternalCommandResultDto.of(false, e.getMessage());
    }

    if (healthCheckResult.getStatus() == HealthCheckStatus.DOWN) {
      return HealthCheckInternalCommandResultDto.of(
          false,
          new ApiServerException(
                  new ApiServerNotAvailableException(healthCheckResult.getChecks().toString())
                      .getMessage())
              .getMessage());
    }

    return HealthCheckInternalCommandResultDto.of(true, null);
  }
}
