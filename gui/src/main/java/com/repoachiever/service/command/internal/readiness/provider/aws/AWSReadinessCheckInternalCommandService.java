package com.repoachiever.service.command.internal.readiness.provider.aws;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.ReadinessCheckInternalCommandResultDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AWSReadinessCheckInternalCommandService
    implements ICommand<ReadinessCheckInternalCommandResultDto> {
  @Autowired private ConfigService configService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  @Autowired private ReadinessCheckClientCommandService readinessCheckClientCommandService;

  /**
   * @see ICommand
   */
  @Override
  public ReadinessCheckInternalCommandResultDto process() {
    ConfigEntity.Cloud.AWSCredentials credentials =
        CredentialsConverter.convert(
            configService.getConfig().getCloud().getCredentials(),
            ConfigEntity.Cloud.AWSCredentials.class);

    ValidationSecretsApplicationDto validationSecretsApplicationDto =
        ValidationSecretsApplicationDto.of(Provider.AWS, credentials.getFile());

    ValidationSecretsApplicationResult validationSecretsApplicationResult;
    try {
      validationSecretsApplicationResult =
          secretsAcquireClientCommandService.process(validationSecretsApplicationDto);
    } catch (ApiServerException e) {
      return ReadinessCheckInternalCommandResultDto.of(false, e.getMessage());
    }

    if (validationSecretsApplicationResult.getValid()) {
      CredentialsFields credentialsFields =
          CredentialsFields.of(
              AWSSecrets.of(
                  validationSecretsApplicationResult.getSecrets().getAccessKey(),
                  validationSecretsApplicationResult.getSecrets().getSecretKey()),
              credentials.getRegion());

      ReadinessCheckApplication readinessCheckApplication =
          ReadinessCheckApplication.of(Provider.AWS, credentialsFields);

      ReadinessCheckResult readinessCheckResult;
      try {
        readinessCheckResult =
            readinessCheckClientCommandService.process(readinessCheckApplication);
      } catch (ApiServerException e) {
        return ReadinessCheckInternalCommandResultDto.of(false, e.getMessage());
      }

      if (readinessCheckResult.getStatus() == ReadinessCheckStatus.DOWN) {
        return ReadinessCheckInternalCommandResultDto.of(
            false,
            new ApiServerException(
                    new ApiServerNotAvailableException(readinessCheckResult.getData().toString())
                        .getMessage())
                .getMessage());
      }

      return ReadinessCheckInternalCommandResultDto.of(true, null);
    } else {
      return ReadinessCheckInternalCommandResultDto.of(
          false, new CloudCredentialsValidationException().getMessage());
    }
  }
}
