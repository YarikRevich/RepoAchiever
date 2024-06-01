package com.repoachiever.service.hand.internal.health;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.service.client.health.HealthClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/**
 * Represents health internal command service.
 */
@Service
public class HealthCheckInternalCommandService implements ICommand<HealthCheckResult, ConfigEntity> {
  /**
   * @see ICommand
   */
  @Override
  public HealthCheckResult process(ConfigEntity config) throws ApiServerOperationFailureException {
    HealthClientService healthClientService =
            new HealthClientService(config.getApiServer().getHost());

    return healthClientService.process(null);
  }
}
