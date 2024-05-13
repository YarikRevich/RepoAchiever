package com.repoachiever.service.command.common;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contains helpful tools used for general command configuration.
 */
public class CommandConfigurationHelper {
    /**
     * Composes environment variables.
     *
     * @param attributes attributes to be included.
     * @return composed environment variables.
     */
    public static String getEnvironmentVariables(Map<String, String> attributes) {
        return attributes.entrySet().stream()
                .map(element -> String.format("%s='%s'", element.getKey(), element.getValue()))
                .collect(Collectors.joining(" "));
    }

    /**
     * Composes Docker name declaration.
     *
     * @param attribute attribute to be included.
     * @return composed Docker name declaration.
     */
    public static String getDockerName(String attribute) {
        return String.format("--name '%s'", attribute);
    }

    /**
     * Composes Docker network declaration.
     *
     * @param attribute attribute to be included.
     * @return composed Docker network declaration.
     */
    public static String getDockerNetwork(String attribute) {
        return String.format("--network '%s'", attribute);
    }

    /**
     * Composes Docker volumes declaration.
     *
     * @param attributes attributes to be included.
     * @return composed Docker volumes declaration.
     */
    public static String getDockerVolumes(Map<String, String> attributes) {
        return attributes.entrySet().stream()
                .map(element -> String.format("-v %s:%s", element.getKey(), element.getValue()))
                .collect(Collectors.joining(" "));
    }

    /**
     * Composes Docker environment variables declaration.
     *
     * @param attributes attributes to be included.
     * @return composed Docker environment variables declaration.
     */
    public static String getDockerEnvironmentVariables(Map<String, String> attributes) {
        return attributes.entrySet().stream()
                .map(element -> String.format("-e %s=%s", element.getKey(), element.getValue()))
                .collect(Collectors.joining(" "));
    }

    /**
     * Composes Docker command arguments' declaration.
     *
     * @param attributes attributes to be included.
     * @return composed Docker command arguments' declaration.
     */
    public static String getDockerCommandArguments(Map<String, String> attributes) {
        return attributes.entrySet().stream()
                .map(element -> String.format("--%s=%s", element.getKey(), element.getValue()))
                .collect(Collectors.joining(" "));
    }

    /**
     * Composes Docker command options declaration.
     *
     * @param attributes attributes to be included.
     * @return composed Docker command options declaration.
     */
    public static String getDockerCommandOptions(List<String> attributes) {
        return attributes.
                stream().
                map(element -> String.format("--%s", element))
                .collect(Collectors.joining(" "));
    }

    /**
     * Composes Docker port mappings.
     *
     * @param attributes attributes to be included.
     * @return composed Docker port mappings.
     */
    public static String getDockerPorts(Map<Integer, Integer> attributes) {
        return attributes.entrySet().stream()
                .map(element -> String.format("-p 0.0.0.0:%d:%d", element.getKey(), element.getValue()))
                .collect(Collectors.joining(" "));
    }
}
