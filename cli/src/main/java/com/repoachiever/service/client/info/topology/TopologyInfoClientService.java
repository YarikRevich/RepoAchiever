package com.repoachiever.service.client.info.topology;

import com.repoachiever.ApiClient;
import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.exception.ApiServerNotAvailableException;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.TopologyInfoApplication;
import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.service.client.common.IClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/** Represents implementation for v1InfoTopologyPost endpoint of InfoResourceApi. */
public class TopologyInfoClientService implements IClient<List<TopologyInfoUnit>, TopologyInfoApplication> {
    private final InfoResourceApi infoResourceApi;

    public TopologyInfoClientService(String host) {
        ApiClient apiClient = new ApiClient().setBasePath(host);

        this.infoResourceApi = new InfoResourceApi(apiClient);
    }

    /**
     * @see IClient
     */
    public List<TopologyInfoUnit> process(TopologyInfoApplication input) throws ApiServerOperationFailureException {
        try {
            return infoResourceApi.v1InfoTopologyPost(input).collectList().block();
        } catch (WebClientResponseException e) {
            throw new ApiServerOperationFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new ApiServerOperationFailureException(new ApiServerNotAvailableException(e.getMessage()).getMessage());
        }
    }
}