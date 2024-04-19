package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ValidationResourceApi;
import com.repoachiever.dto.ValidationScriptApplicationDto;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.ValidationScriptApplication;
import com.repoachiever.model.ValidationScriptApplicationResult;
import com.repoachiever.service.client.common.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents script validation client command service. */
@Service
public class ScriptAcquireClientCommandService
    implements IClientCommand<ValidationScriptApplicationResult, ValidationScriptApplicationDto> {
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
  public ValidationScriptApplicationResult process(ValidationScriptApplicationDto input)
      throws ApiServerException {
    try {
      return validationResourceApi
          .v1ScriptAcquirePost(ValidationScriptApplication.of(input.getFileContent()))
          .block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
