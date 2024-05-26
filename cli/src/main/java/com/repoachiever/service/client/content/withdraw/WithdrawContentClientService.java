package com.repoachiever.service.client.content.withdraw;

import com.repoachiever.ApiClient;
import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.model.ContentWithdrawal;
import com.repoachiever.service.client.common.IClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Represents implementation for v1ContentPost endpoint of ContentResourceApi.
 */
public class WithdrawContentClientService implements IClient<Void, ContentWithdrawal> {
    private final ContentResourceApi contentResourceApi;

    public WithdrawContentClientService(String host) {
        ApiClient apiClient = new ApiClient().setBasePath(host);

        this.contentResourceApi = new ContentResourceApi(apiClient);
    }

    /**
     * @see IClient
     */
    @Override
    public Void process(ContentWithdrawal input) throws ApiServerOperationFailureException {
        try {
            return contentResourceApi.v1ContentWithdrawDelete(input).block();
        } catch (WebClientResponseException e) {
            throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}