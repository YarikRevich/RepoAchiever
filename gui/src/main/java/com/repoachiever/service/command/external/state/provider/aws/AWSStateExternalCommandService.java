package com.repoachiever.service.command.external.state.provider.aws;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.StateExternalCommandResultDto;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.command.LogsClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start external command service for AWS provider. */
@Service
public class AWSStateExternalCommandService implements ICommand<StateExternalCommandResultDto> {
  @Autowired private ConfigService configService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  @Autowired private LogsClientCommandService logsClientCommandService;

  /**
   * @see ICommand
   */
  @Override
  public StateExternalCommandResultDto process() {
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
      return StateExternalCommandResultDto.of(null, false, e.getMessage());
    }

    if (validationSecretsApplicationResult.getValid()) {
      CredentialsFields credentialsFields =
          CredentialsFields.of(
              AWSSecrets.of(
                  validationSecretsApplicationResult.getSecrets().getAccessKey(),
                  validationSecretsApplicationResult.getSecrets().getSecretKey()),
              credentials.getRegion());

      TopicLogsResult topicLogsResult;

      TopicLogsApplication topicLogsApplication =
          TopicLogsApplication.of(Provider.AWS, credentialsFields);

      try {
        topicLogsResult = logsClientCommandService.process(topicLogsApplication);
      } catch (ApiServerException e) {
        return StateExternalCommandResultDto.of(null, false, e.getMessage());
      }

      return StateExternalCommandResultDto.of(topicLogsResult, true, null);
    } else {
      return StateExternalCommandResultDto.of(
          null, false, new CloudCredentialsValidationException().getMessage());
    }
  }
}
