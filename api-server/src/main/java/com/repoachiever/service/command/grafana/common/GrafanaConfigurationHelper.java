package com.repoachiever.service.command.grafana.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;

/** Contains helpful tools used for Grafana deployment configuration. */
public class GrafanaConfigurationHelper {
    /**
     * Composes environment variables for Grafana deployment.
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