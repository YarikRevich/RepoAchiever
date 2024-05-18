package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.HealthResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.ReadinessCheckApplication;
import com.repoachiever.model.ReadinessCheckResult;
import com.repoachiever.service.client.common.IClient;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents readiness check client command service. */
@Service
public class ReadinessCheckClientCommandService
    implements IClient<ReadinessCheckResult, ReadinessCheckApplication> {
  private final HealthResourceApi healthResourceApi;

  public ReadinessCheckClientCommandService(@Autowired ConfigService configService) {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.healthResourceApi = new HealthResourceApi(apiClient);
  }

  /**
   * @see IClient
   */
  public ReadinessCheckResult process(ReadinessCheckApplication input) throws ApiServerException {
    try {
      return healthResourceApi.v1ReadinessPost(input).block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
