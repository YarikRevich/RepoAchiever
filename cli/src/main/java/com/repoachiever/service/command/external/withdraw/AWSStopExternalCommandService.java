package com.repoachiever.service.command.external.withdraw;

import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.model.AWSSecrets;
import com.repoachiever.model.CredentialsFields;
import com.repoachiever.model.DestructionRequest;
import com.repoachiever.model.Provider;
import com.repoachiever.model.TerraformDestructionApplication;
import com.repoachiever.model.ValidationSecretsApplicationResult;
import com.repoachiever.service.client.command.DestroyClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** */
@Service
public class AWSStopExternalCommandService implements ICommand {
  private static final Logger logger = LogManager.getLogger(AWSStopExternalCommandService.class);

  @Autowired private ConfigService configService;

  @Autowired private DestroyClientCommandService destroyClientCommandService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

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

      destroyClientCommandService.process(terraformDestructionApplication);

      visualizationState.getLabel().pushNext();
    } else {
      logger.fatal(new CloudCredentialsValidationException().getMessage());
    }
  }
}
