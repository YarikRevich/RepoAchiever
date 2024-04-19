package com.repoachiever.service.command.external.start.provider.aws;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.StartExternalCommandResultDto;
import com.repoachiever.dto.ValidationScriptApplicationDto;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.exception.ScriptDataValidationException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.command.ApplyClientCommandService;
import com.repoachiever.service.client.command.ScriptAcquireClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start external command service for AWS provider. */
@Service
public class AWSStartExternalCommandService implements ICommand<StartExternalCommandResultDto> {
  @Autowired private ConfigService configService;

  @Autowired private ApplyClientCommandService applyClientCommandService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  @Autowired private ScriptAcquireClientCommandService scriptAcquireClientCommandService;

  /**
   * @see ICommand
   */
  @Override
  public StartExternalCommandResultDto process() {
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
      return StartExternalCommandResultDto.of(false, e.getMessage());
    }

    if (validationSecretsApplicationResult.getValid()) {
      List<DeploymentRequest> requests = new ArrayList<>();

      for (ConfigEntity.Request request : configService.getConfig().getRequests()) {
        try {
          requests.add(DeploymentRequest.of(
                  request.getName(),
                  Files.readString(Paths.get(request.getFile())),
                  request.getFrequency()));
        } catch (IOException e) {
          return StartExternalCommandResultDto.of(false, new ScriptDataValidationException(e.getMessage()).getMessage());
        }
      }

      ValidationScriptApplicationDto validationScriptApplicationDto =
          ValidationScriptApplicationDto.of(
              requests.stream().map(DeploymentRequest::getScript).toList());

      ValidationScriptApplicationResult validationScriptApplicationResult;
      try {
        validationScriptApplicationResult =
            scriptAcquireClientCommandService.process(validationScriptApplicationDto);
      } catch (ApiServerException e) {
        return StartExternalCommandResultDto.of(false, e.getMessage());
      }

      if (validationScriptApplicationResult.getValid()) {
        CredentialsFields credentialsFields =
            CredentialsFields.of(
                AWSSecrets.of(
                    validationSecretsApplicationResult.getSecrets().getAccessKey(),
                    validationSecretsApplicationResult.getSecrets().getSecretKey()),
                credentials.getRegion());

        TerraformDeploymentApplication terraformDeploymentApplication =
            TerraformDeploymentApplication.of(requests, Provider.AWS, credentialsFields);

        try {
          applyClientCommandService.process(terraformDeploymentApplication);
        } catch (ApiServerException e) {
          return StartExternalCommandResultDto.of(false, e.getMessage());
        }

        return StartExternalCommandResultDto.of(true, null);
      } else {
        return StartExternalCommandResultDto.of(
            false, new ScriptDataValidationException().getMessage());
      }
    } else {
      return StartExternalCommandResultDto.of(
          false, new CloudCredentialsValidationException().getMessage());
    }
  }
}
