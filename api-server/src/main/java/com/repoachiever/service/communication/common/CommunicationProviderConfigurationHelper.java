package com.repoachiever.service.communication.common;

/** Contains helpful tools used for communication provider configuration. */
public class CommunicationProviderConfigurationHelper {
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