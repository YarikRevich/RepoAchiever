package com.repoachiever.service.client.info.version;

import com.repoachiever.ApiClient;
import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.common.IClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/** Represents implementation for v1InfoVersionGet endpoint of InfoResourceApi. */
public class VersionInfoClientService implements IClient<VersionInfoResult, Void> {
  private final InfoResourceApi infoResourceApi;

  public VersionInfoClientService(String host) {
    ApiClient apiClient = new ApiClient(WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                    HttpClient.create().followRedirect(true)))
            .build())
            .setBasePath(host);

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
