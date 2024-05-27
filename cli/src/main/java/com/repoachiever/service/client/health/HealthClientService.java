package com.repoachiever.service.client.health;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.api.HealthResourceApi;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.service.client.common.IClient;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/**
 * Represents implementation for v1HealthGet endpoint of HealthResourceApi.
 */
public class HealthClientService implements IClient<HealthCheckResult, Void> {
    private final HealthResourceApi healthResourceApi;

    public HealthClientService(String host) {
        ApiClient apiClient = new ApiClient(WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)))
                .build())
                .setBasePath(host);

        this.healthResourceApi = new HealthResourceApi(apiClient);
    }

    /**
     * @see IClient
     */
    public HealthCheckResult process(Void input) throws ApiServerOperationFailureException {
        try {
            return healthResourceApi.v1HealthGet().block();
        } catch (WebClientResponseException e) {
            throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}
