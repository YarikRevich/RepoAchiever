package com.repoachiever.service.integration.communication.common;

/** Contains helpful tools used for RepoAchiever Cluster communication provider configuration. */
public class ProviderCommunicationConfigurationHelper {
    /**
     * Composes binding URI declaration for RMI.
     *
     * @param registryPort given registry port.
     * @param suffix given binding suffix.
     * @return composed binding URI declaration for RMI.
     */
    public static String getBindName(Integer registryPort, String suffix) {
        return String.format("rmi://localhost:%d/%s", registryPort, suffix);
    }
}