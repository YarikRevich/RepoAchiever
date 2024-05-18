package com.repoachiever.service.client.content.apply;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.service.client.common.IClient;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.repoachiever.model.ContentApplication;

/** Represents content application operation service. */
@Service
public class ApplyContentClientService implements IClient<Void, ContentApplication> {
    @Autowired
    private ConfigService configService;

    private ContentResourceApi contentResourceApi;

    @PostConstruct
    private void configure() {
        ApiClient apiClient =
                new ApiClient().setBasePath(configService.getConfig().getApiServer().getHost());

        this.contentResourceApi = new ContentResourceApi(apiClient);
    }

    /**
     * @see IClient
     */
    @Override
    public Void process(ContentApplication input)
            throws ApiServerException {
        try {
            return contentResourceApi
                    .v1ContentApplyPost(input)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ApiServerException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}
