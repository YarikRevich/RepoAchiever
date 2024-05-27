package com.repoachiever.service.client.content.apply;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.service.client.common.IClient;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.repoachiever.model.ContentApplication;
import reactor.netty.http.client.HttpClient;

/**
 * Represents implementation for v1ContentApplyPost endpoint of ContentResourceApi.
 */
public class ApplyContentClientService implements IClient<Void, ContentApplication> {
    private final ContentResourceApi contentResourceApi;

    public ApplyContentClientService(String host) {
        ApiClient apiClient = new ApiClient(WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(true)))
                .build())
                .setBasePath(host);

        this.contentResourceApi = new ContentResourceApi(apiClient);
    }

    /**
     * @see IClient
     */
    @Override
    public Void process(ContentApplication input)
            throws ApiServerOperationFailureException {
        try {
            return contentResourceApi.v1ContentApplyPost(input).block();
        } catch (WebClientResponseException e) {
            throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}
