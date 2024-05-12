package com.repoachiever.service.integration.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.nio.file.Path;
import java.util.HashMap;

/**
 * Contains helpful tools used for integrations processing configuration.
 */
public class IntegrationConfigurationHelper {
    /**
     * Composes running state file removal suggestion message.
     *
     * @param path path of the running state file.
     * @return composed running state file removal suggestion message.
     */
    public static String getRunningStateFileRemovalSuggestionMessage(Path path) {
        return String.format("If the exception is not relevant, please remove %s state file", path);
    }
}