package com.repoachiever.service.command.nodeexporter.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Contains helpful tools used for Prometheus NodeExporter deployment configuration.
 */
public class NodeExporterConfigurationHelper {
    /**
     * Composes Prometheus Node Exporter Docker volumes declaration.
     *
     * @return composed Prometheus Node Exporter Docker volumes declaration.
     */
    public static String getDockerVolumes() {
        return CommandConfigurationHelper.getDockerVolumes(
                new HashMap<>() {
                    {
                        put("/proc", "/host/proc:ro");
                        put("/sys", "/host/sys:ro");
                        put("/", "/rootfs:ro");
                    }
                });
    }

    /**
     * Composes Prometheus Node Exporter Docker command arguments' declaration.
     *
     * @param port given Prometheus Node Exporter listening port.
     * @return composed Prometheus Node Exporter Docker command arguments' declaration.
     */
    public static String getDockerCommandArguments(Integer port) {
        return CommandConfigurationHelper.getDockerCommandArguments(
                new LinkedHashMap<>() {
                    {
                        put("path.procfs", "/host/proc");
                        put("path.sysfs", "/host/sys");
                        put("collector.filesystem.ignored-mount-points", "\"^/(sys|proc|dev|host|etc|rootfs/var/lib/docker/containers|rootfs/var/lib/docker/overlay2|rootfs/run/docker/netns|rootfs/var/lib/docker/aufs)($$|/)\"");
                        put("web.listen-address", String.format("0.0.0.0:%d", port));
                    }
                });
    }
}