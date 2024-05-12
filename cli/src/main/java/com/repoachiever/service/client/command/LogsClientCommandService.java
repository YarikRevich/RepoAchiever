package com.repoachiever.service.client.command;

import com.repoachiever.ApiClient;
import com.repoachiever.api.TopicResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.TopicLogsApplication;
import com.repoachiever.model.TopicLogsResult;
import com.repoachiever.service.client.IClientCommand;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/** Represents logs topic client command service. */
@Service
public class LogsClientCommandService
    implements IClientCommand<TopicLogsResult, TopicLogsApplication> {
  private final TopicResourceApi topicResourceApi;

  public LogsClientCommandService(@Autowired ConfigService configService) {
    ApiClient apiClient =
        new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

    this.topicResourceApi = new TopicResourceApi(apiClient);
  }

  /**
   * @see IClientCommand
   */
  @Override
  public TopicLogsResult process(TopicLogsApplication input) throws ApiServerException {
    try {
      return topicResourceApi.v1TopicLogsPost(input).block();
    } catch (WebClientResponseException e) {
      throw new ApiServerException(e.getResponseBodyAsString());
    } catch (WebClientRequestException e) {
      throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
    }
  }
}
