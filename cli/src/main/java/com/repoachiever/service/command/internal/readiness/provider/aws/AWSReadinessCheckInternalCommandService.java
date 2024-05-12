package com.repoachiever.service.command.internal.readiness.provider.aws;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.command.ReadinessCheckClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AWSReadinessCheckInternalCommandService implements ICommand {
  private static final Logger logger =
      LogManager.getLogger(AWSReadinessCheckInternalCommandService.class);

  @Autowired private ConfigService configService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  @Autowired private ReadinessCheckClientCommandService readinessCheckClientCommandService;

  @Autowired private VisualizationState visualizationState;

  /**
   * @see ICommand
   */
  @Override
  public void process() throws ApiServerException {
    visualizationState.getLabel().pushNext();

    ConfigEntity.Cloud.AWSCredentials credentials =
        CredentialsConverter.convert(
            configService.getConfig().getCloud().getCredentials(),
            ConfigEntity.Cloud.AWSCredentials.class);

    ValidationSecretsApplicationDto validationSecretsApplicationDto =
        ValidationSecretsApplicationDto.of(Provider.AWS, credentials.getFile());

    ValidationSecretsApplicationResult validationSecretsApplicationResult =
        secretsAcquireClientCommandService.process(validationSecretsApplicationDto);

    if (validationSecretsApplicationResult.getValid()) {
      CredentialsFields credentialsFields =
          CredentialsFields.of(
              AWSSecrets.of(
                  validationSecretsApplicationResult.getSecrets().getAccessKey(),
                  validationSecretsApplicationResult.getSecrets().getSecretKey()),
              credentials.getRegion());

      ReadinessCheckApplication readinessCheckApplication =
          ReadinessCheckApplication.of(Provider.AWS, credentialsFields);

      ReadinessCheckResult readinessCheckResult =
          readinessCheckClientCommandService.process(readinessCheckApplication);

      if (readinessCheckResult.getStatus() == ReadinessCheckStatus.DOWN) {
        throw new ApiServerException(
            new ApiServerNotAvailableException(readinessCheckResult.getData().toString())
                .getMessage());
      }
    } else {
      logger.fatal(new CloudCredentialsValidationException().getMessage());
    }
  }
}
