package com.repoachiever.service.command.cluster.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;

/**
 * Contains helpful tools used for Grafana deployment configuration.
 */
public class ClusterConfigurationHelper {
    /**
     * Composes environment variables for Grafana deployment.
     *
     * @param clusterContext RepoAchiever Cluster context used for cluster configuration.
     * @return composed environment variables.
     */
    public static String getEnvironmentVariables(String clusterContext) {
        return CommandConfigurationHelper.getEnvironmentVariables(
                new HashMap<>() {
                    {
                        put("REPOACHIEVER_CLUSTER_CONTEXT", clusterContext);
                    }
                });
    }
}