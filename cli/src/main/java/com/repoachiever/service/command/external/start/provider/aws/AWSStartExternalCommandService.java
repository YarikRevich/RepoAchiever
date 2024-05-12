package com.repoachiever.service.command.external.start.provider.aws;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.ValidationScriptApplicationDto;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.exception.ScriptDataValidationException;
import com.repoachiever.exception.VersionMismatchException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.command.ApplyClientCommandService;
import com.repoachiever.service.client.command.ScriptAcquireClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.client.command.VersionClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.state.VisualizationState;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start external command service for AWS provider. */
@Service
public class AWSStartExternalCommandService implements ICommand {
  private static final Logger logger = LogManager.getLogger(AWSStartExternalCommandService.class);

  @Autowired private ConfigService configService;

  @Autowired private PropertiesEntity properties;

  @Autowired private ApplyClientCommandService applyClientCommandService;

  @Autowired private VersionClientCommandService versionClientCommandService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  @Autowired private ScriptAcquireClientCommandService scriptAcquireClientCommandService;

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
      visualizationState.getLabel().pushNext();

      ApplicationInfoResult applicationInfoResult = versionClientCommandService.process(null);

      if (!applicationInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
        throw new ApiServerException(new VersionMismatchException().getMessage());
      }

      List<DeploymentRequest> requests =
          configService.getConfig().getRequests().stream()
              .map(
                  element -> {
                    try {
                      return DeploymentRequest.of(
                          element.getName(),
                          Files.readString(Paths.get(element.getFile())),
                          element.getFrequency());
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                  })
              .toList();

      ValidationScriptApplicationDto validationScriptApplicationDto =
          ValidationScriptApplicationDto.of(
              requests.stream().map(DeploymentRequest::getScript).toList());

      ValidationScriptApplicationResult validationScriptApplicationResult =
          scriptAcquireClientCommandService.process(validationScriptApplicationDto);

      if (validationScriptApplicationResult.getValid()) {
        visualizationState.getLabel().pushNext();

        CredentialsFields credentialsFields =
            CredentialsFields.of(
                AWSSecrets.of(
                    validationSecretsApplicationResult.getSecrets().getAccessKey(),
                    validationSecretsApplicationResult.getSecrets().getSecretKey()),
                credentials.getRegion());

        TerraformDeploymentApplication terraformDeploymentApplication =
            TerraformDeploymentApplication.of(requests, Provider.AWS, credentialsFields);

        applyClientCommandService.process(terraformDeploymentApplication);

        visualizationState.getLabel().pushNext();
      } else {
        logger.fatal(new ScriptDataValidationException().getMessage());
      }
    } else {
      logger.fatal(new CloudCredentialsValidationException().getMessage());
    }
  }
}
