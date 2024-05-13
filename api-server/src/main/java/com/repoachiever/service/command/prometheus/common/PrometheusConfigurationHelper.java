package com.repoachiever.service.command.prometheus.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;


/**
 * Contains helpful tools used for Prometheus deployment configuration.
 */
public class PrometheusConfigurationHelper {
    /**
     * Composes Prometheus Docker additional parameters.
     *
     * @return composed Docker additional parameters.
     */
    public static String getDockerParameters() {
        return "--add-host=host.docker.internal:host-gateway";
    }

    /**
     * Composes Prometheus Docker volumes declaration.
     *
     * @param configLocation given Prometheus local config directory location.
     * @param internalLocation given Prometheus local internal directory location.
     * @return composed Prometheus Docker volumes declaration.
     */
    public static String getDockerVolumes(String configLocation, String internalLocation) {
        return CommandConfigurationHelper.getDockerVolumes(
                new HashMap<>() {
                    {
                        put(configLocation, "/etc/prometheus/");
                        put(internalLocation, "/prometheus");
                    }
                });
    }

    /**
     * Composes Prometheus Docker command arguments' declaration.
     *
     * @param port given Prometheus listening port.
     * @return composed Prometheus Docker command arguments' declaration.
     */
    public static String getDockerCommandArguments(Integer port) {
        return CommandConfigurationHelper.getDockerCommandArguments(
                new LinkedHashMap<>() {
                    {
                        put("config.file", "/etc/prometheus/prometheus.yml");
                        put("storage.tsdb.path", "/prometheus");
                        put("web.console.libraries", "/usr/share/prometheus/console_libraries");
                        put("web.console.templates", "/usr/share/prometheus/consoles");
                        put("web.listen-address", String.format("0.0.0.0:%d", port));
                    }
                });
    }

    /**
     * Composes Prometheus Docker command options declaration.
     *
     * @return composed Prometheus Docker command options declaration.
     */
    public static String getDockerCommandOptions() {
        return CommandConfigurationHelper.getDockerCommandOptions(
                List.of("web.enable-lifecycle",
                        "web.enable-admin-api"));
    }
}



