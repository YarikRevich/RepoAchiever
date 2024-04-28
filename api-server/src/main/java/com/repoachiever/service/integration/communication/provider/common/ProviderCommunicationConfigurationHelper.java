package com.repoachiever.service.integration.communication.provider.common;

import com.repoachiever.service.command.common.CommandConfigurationHelper;

import java.util.HashMap;

/** Contains helpful tools used for RepoAchiever API Server communication provider configuration. */
public class ProviderCommunicationConfigurationHelper {
    /**
     * Composes binding URI declaration for RMI.
     *
     * @param registryPort given registry port.
     * @param suffix given binding suffix.
     * @return composed binding URI declaration for RMI.
     */
    public static String getBindName(Integer registryPort, String suffix) {
        return String.format("//localhost:%d/%s", registryPort, suffix);
    }
}