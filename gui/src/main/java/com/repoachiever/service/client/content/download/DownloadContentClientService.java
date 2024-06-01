package com.repoachiever.service.client.content.download;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentDownload;
import com.repoachiever.service.client.common.IClient;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/**
 * Represents implementation for v1ContentDownloadPost endpoint of ContentResourceApi.
 */
public class DownloadContentClientService implements IClient<byte[], ContentDownload> {
    private final ContentResourceApi contentResourceApi;

    public DownloadContentClientService(String host) {
        ApiClient apiClient = new ApiClient(WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs()
                                .maxInMemorySize(-1))
                        .build())
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
    public byte[] process(ContentDownload input) throws ApiServerOperationFailureException {
        try {
            return contentResourceApi
                    .v1ContentDownloadPost(input)
                    .block();
        } catch (WebClientResponseException e) {
            throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}