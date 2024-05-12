package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.ApplicationInfoResult;
import com.repoachiever.service.client.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents version information client command service. */
@Service
public class VersionClientCommandService implements IClientCommand<ApplicationInfoResult, Void> {
  private final InfoResourceApi infoResourceApi;

  public VersionClientCommandService(@Autowired ConfigService configService) {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.infoResourceApi = new InfoResourceApi(apiClient);
  }

  /**
   * @see IClientCommand
   */
  public ApplicationInfoResult process(Void input) throws ApiServerException {
    try {
      return infoResourceApi.v1InfoVersionGet().block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
