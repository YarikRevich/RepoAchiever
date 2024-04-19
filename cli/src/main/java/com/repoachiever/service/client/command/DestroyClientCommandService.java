package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.TerraformResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.TerraformDestructionApplication;
import com.repoachiever.service.client.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents destroy client command service. */
@Service
public class DestroyClientCommandService
    implements IClientCommand<Void, TerraformDestructionApplication> {
  private final TerraformResourceApi terraformResourceApi;

  public DestroyClientCommandService(@Autowired ConfigService configService) {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.terraformResourceApi = new TerraformResourceApi(apiClient);
  }

  /**
   * @see IClientCommand
   */
  public Void process(TerraformDestructionApplication input) throws ApiServerException {
    try {
      return terraformResourceApi.v1TerraformDestroyPost(input).block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
