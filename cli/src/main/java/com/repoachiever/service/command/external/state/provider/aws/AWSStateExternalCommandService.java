package com.repoachiever.service.command.external.state.provider.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repoachiever.converter.CredentialsConverter;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.CloudCredentialsValidationException;
import com.repoachiever.model.*;
import com.repoachiever.service.client.command.LogsClientCommandService;
import com.repoachiever.service.client.command.SecretsAcquireClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start external command service for AWS provider. */
@Service
public class AWSStateExternalCommandService implements ICommand {
  private static final Logger logger = LogManager.getLogger(AWSStateExternalCommandService.class);

  @Autowired private ConfigService configService;

  @Autowired private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

  @Autowired private LogsClientCommandService logsClientCommandService;

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

      TopicLogsResult topicLogsResult;

      TopicLogsApplication topicLogsApplication =
          TopicLogsApplication.of(Provider.AWS, credentialsFields);

      topicLogsResult = logsClientCommandService.process(topicLogsApplication);

      visualizationState.getLabel().pushNext();

      ObjectMapper mapper = new ObjectMapper();
      topicLogsResult
          .getResult()
          .forEach(
              element -> {
                try {
                  visualizationState.addResult(mapper.writeValueAsString(element));
                } catch (JsonProcessingException e) {
                  throw new RuntimeException(e);
                }
              });
    } else {
      logger.fatal(new CloudCredentialsValidationException().getMessage());
    }
  }
}
