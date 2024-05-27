package com.repoachiever.service.command.external.withdraw.provider.aws;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.StopExternalCommandResultDto;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.command.DestroyClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** */
@Service
public class AWSStopExternalCommandService implements ICommand<StopExternalCommandResultDto> {
  @Autowired private ConfigService configService;

  @Autowired private DestroyClientCommandService destroyClientCommandService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  /**
   * @see ICommand
   */
  @Override
  public StopExternalCommandResultDto process() {
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
      return StopExternalCommandResultDto.of(false, e.getMessage());
    }

    if (validationSecretsApplicationResult.getValid()) {
      CredentialsFields credentialsFields =
          CredentialsFields.of(
              AWSSecrets.of(
                  validationSecretsApplicationResult.getSecrets().getAccessKey(),
                  validationSecretsApplicationResult.getSecrets().getSecretKey()),
              credentials.getRegion());

      TerraformDestructionApplication terraformDestructionApplication =
          TerraformDestructionApplication.of(
              configService.getConfig().getRequests().stream()
                  .map(element -> DestructionRequest.of(element.getName()))
                  .toList(),
              Provider.AWS,
              credentialsFields);

      try {
        destroyClientCommandService.process(terraformDestructionApplication);
      } catch (ApiServerException e) {
        return StopExternalCommandResultDto.of(false, e.getMessage());
      }

      return StopExternalCommandResultDto.of(true, null);
    } else {
      return StopExternalCommandResultDto.of(
          false, new CloudCredentialsValidationException().getMessage());
    }
  }
}
