package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.TerraformResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.TerraformDeploymentApplication;
import com.repoachiever.model.TerraformDeploymentApplicationResult;
import com.repoachiever.service.client.common.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents apply client command service. */
@Service
public class ApplyClientCommandService
    implements IClientCommand<
        TerraformDeploymentApplicationResult, TerraformDeploymentApplication> {
  @Autowired private ConfigService configService;

  private TerraformResourceApi terraformResourceApi;

  /**
   * @see IClientCommand
   */
  @Override
  @PostConstruct
  public void configure() {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.terraformResourceApi = new TerraformResourceApi(apiClient);
  }

  /**
   * @see IClientCommand
   */
  @Override
  public TerraformDeploymentApplicationResult process(TerraformDeploymentApplication input)
      throws ApiServerException {
    try {
      return terraformResourceApi.v1TerraformApplyPost(input).block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
