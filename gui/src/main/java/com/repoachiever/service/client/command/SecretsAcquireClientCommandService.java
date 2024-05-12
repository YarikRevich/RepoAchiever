package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ValidationResourceApi;
import com.repoachiever.dto.ValidationSecretsApplicationDto;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.CloudCredentialsFileNotFoundException;
import com.repoachiever.model.Provider;
import com.repoachiever.model.ValidationSecretsApplication;
import com.repoachiever.model.ValidationSecretsApplicationResult;
import com.repoachiever.service.client.common.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents secrets validation client command service. */
@Service
public class SecretsAcquireClientCommandService
    implements IClientCommand<ValidationSecretsApplicationResult, ValidationSecretsApplicationDto> {
  @Autowired private ConfigService configService;

  private ValidationResourceApi validationResourceApi;

  /**
   * @see IClientCommand
   */
  @Override
  @PostConstruct
  public void configure() {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.validationResourceApi = new ValidationResourceApi(apiClient);
  }

  /**
   * @see IClientCommand
   */
  @Override
  public ValidationSecretsApplicationResult process(ValidationSecretsApplicationDto input)
      throws ApiServerException {
    Path filePath = Paths.get(input.getFilePath());

    if (Files.notExists(filePath)) {
      throw new ApiServerException(new CloudCredentialsFileNotFoundException().getMessage());
    }

    String content;

    try {
      content = Files.readString(filePath);
    } catch (IOException e) {
      throw new ApiServerException(e.getMessage());
    }

    try {
      return validationResourceApi
          .v1SecretsAcquirePost(ValidationSecretsApplication.of(Provider.AWS, content))
          .block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getHeaders()).getMessage());
    }
  }
}
