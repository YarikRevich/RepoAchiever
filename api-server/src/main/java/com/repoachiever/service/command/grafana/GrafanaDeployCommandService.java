package com.repoachiever.service.command.grafana;

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
            String name, String image, Integer port, String configLocation, String internalLocation) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker run -d %s %s --name %s %s",
                    GrafanaConfigurationHelper.getDockerVolumes(configLocation, internalLocation),
                    GrafanaConfigurationHelper.getDockerPorts(port),
                    name,
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
