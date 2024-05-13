package com.repoachiever.service.command.grafana.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** Contains helpful tools used for Grafana deployment configuration. */
public class GrafanaConfigurationHelper {
    /**
     * Composes Grafana Docker volumes declaration.
     *
     * @param configLocation given Grafana local config directory location.
     * @param internalLocation given Grafana local internal directory location.
     * @return composed Grafana Docker volumes declaration.
     */
    public static String getDockerVolumes(String configLocation, String internalLocation) {
        return CommandConfigurationHelper.getDockerVolumes(
                new HashMap<>() {
                    {
                        put(configLocation, "/etc/grafana/provisioning/");
                        put(internalLocation, "/var/lib/grafana");
                    }
                });
    }

    /**
     * Composes environment variables for Grafana deployment.
     *
     * @return composed environment variables.
     */
    public static String getDockerEnvironmentVariables() {
        return CommandConfigurationHelper.getDockerEnvironmentVariables(
                new HashMap<>() {
                    {
                        put("GF_SECURITY_ADMIN_USER", "repoachiever");
                        put("GF_SECURITY_ADMIN_PASSWORD", "repoachiever");
                        put("GF_USERS_ALLOW_SIGN_UP", "false");
                    }
                });
    }

    /**
     * Composes Prometheus Docker port mappings.
     *
     * @param port given Prometheus Docker port.
     * @return composed Prometheus Docker port mappings.
     */
    public static String getDockerPorts(Integer port) {
        return CommandConfigurationHelper.getDockerPorts(
                new HashMap<>() {
                    {
                        put(port, 3000);
                    }
                });
    }
}