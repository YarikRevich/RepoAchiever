package com.repoachiever.service.client.content.download;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentDownload;
import com.repoachiever.model.ContentWithdrawal;
import com.repoachiever.service.client.common.IClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;

/**
 * Represents implementation for v1ContentDownloadPost endpoint of ContentResourceApi.
 */
public class DownloadContentClientService implements IClient<File, ContentDownload> {
    private final ContentResourceApi contentResourceApi;

    public DownloadContentClientService(String host) {
        ApiClient apiClient = new ApiClient().setBasePath(host);

        this.contentResourceApi = new ContentResourceApi(apiClient);
    }

    /**
     * @see IClient
     */
    @Override
    public File process(ContentDownload input) throws ApiServerOperationFailureException {
        try {
            return contentResourceApi.v1ContentDownloadPost(input).block();
        } catch (WebClientResponseException e) {
            throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}