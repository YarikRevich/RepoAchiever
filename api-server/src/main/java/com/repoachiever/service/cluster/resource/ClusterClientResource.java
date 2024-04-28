package com.repoachiever.service.cluster.resource;

import jakarta.enterprise.context.ApplicationScoped;
import com.repoachiever.service.client.cluster.IClusterClientService;

/**
 * Represents implementation for RepoAchiever Cluster remote API.
 */
@ApplicationScoped
public class ClusterClientResource {
    /**
     * @see IClusterClientService
     */
    public String retrieveVersion() {
        return "";
    }

    /**
     * @see IClusterClientService
     */
    public Integer retrieveWorkerAmount() {
        return 0;
    }
}
