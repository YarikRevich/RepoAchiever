package com.repoachiever.service.hand.internal.health;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.HealthCheckStatus;
import com.repoachiever.service.client.health.HealthClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/**
 * Represents health internal command service.
 */
@Service
public class HealthCheckInternalCommandService implements ICommand<Boolean, ConfigEntity> {
  /**
   * @see ICommand
   */
  @Override
  public Boolean process(ConfigEntity config) throws ApiServerOperationFailureException {
    HealthClientService healthClientService =
            new HealthClientService(config.getApiServer().getHost());

    HealthCheckResult healthCheckResult = healthClientService.process(null);

    if (healthCheckResult.getStatus() == HealthCheckStatus.DOWN) {
      throw new ApiServerOperationFailureException(
              new ApiServerNotAvailableException(healthCheckResult.getChecks().toString())
                      .getMessage());
    }

    return true;
  }
}
