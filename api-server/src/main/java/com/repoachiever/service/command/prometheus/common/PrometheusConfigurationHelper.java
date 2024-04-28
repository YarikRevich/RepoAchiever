package com.repoachiever.service.command.prometheus.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;
import java.util.UUID;

/** Contains helpful tools used for Prometheus deployment configuration. */
public class PrometheusConfigurationHelper {
    public static String getContainerName() {
        return String.format("repoachiever-prometheus-%s", UUID.randomUUID());
    }

    /**
     * Composes environment variables for Prometheus deployment.
     *
     * @return composed environment variables.
     */
    public static String getEnvironmentVariables() {
        return CommandConfigurationHelper.getEnvironmentVariables(
                new HashMap<>() {
                    {
                        put("GF_PATHS_CONFIG", "");
                    }
                });
    }
}