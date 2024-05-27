package com.repoachiever.service.command.grafana;

import com.repoachiever.service.command.common.CommandConfigurationHelper;
import com.repoachiever.service.command.grafana.common.GrafanaConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Grafana deployment command.
 */
public class GrafanaDeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public GrafanaDeployCommandService(
            String name, String image, Integer port, String network, String configLocation, String internalLocation) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker run -d %s %s %s %s %s %s",
                    GrafanaConfigurationHelper.getDockerVolumes(configLocation, internalLocation),
                    GrafanaConfigurationHelper.getDockerEnvironmentVariables(),
                    GrafanaConfigurationHelper.getDockerPorts(port),
                    CommandConfigurationHelper.getDockerNetwork(network),
                    CommandConfigurationHelper.getDockerName(name),
                    image);
        };
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public SProcessExecutor.OS getOSType() {
        return osType;
    }
}
