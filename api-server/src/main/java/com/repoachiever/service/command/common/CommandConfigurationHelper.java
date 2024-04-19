package com.repoachiever.service.command.common;

import java.util.Map;
import java.util.stream.Collectors;

/** Contains helpful tools used for general command configuration. */
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
}
