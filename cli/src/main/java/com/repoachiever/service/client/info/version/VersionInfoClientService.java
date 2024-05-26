package com.repoachiever.service.client.info.version;

import com.repoachiever.ApiClient;
import com.repoachiever.api.HealthResourceApi;
import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.common.IClient;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents implementation for v1InfoVersionGet endpoint of InfoResourceApi. */
public class VersionInfoClientService implements IClient<VersionInfoResult, Void> {
  private final InfoResourceApi infoResourceApi;

  public VersionInfoClientService(String host) {
    ApiClient apiClient = new ApiClient().setBasePath(host);

    this.infoResourceApi = new InfoResourceApi(apiClient);
  }

  /**
   * @see IClient
   */
  public VersionInfoResult process(Void input) throws ApiServerOperationFailureException {
    try {
      return infoResourceApi.v1InfoVersionGet().block();
    } catch (WebClientResponseException e) {
      throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
