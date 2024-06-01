package com.repoachiever.service.command.prometheus;

import com.repoachiever.service.command.common.CommandConfigurationHelper;
import com.repoachiever.service.command.prometheus.common.PrometheusConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Prometheus deployment command.
 */
public class PrometheusDeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public PrometheusDeployCommandService(
            String name, String image, Integer port, String network, String configLocation, String internalLocation) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker run -d %s %s %s %s %s %s %s",
                    PrometheusConfigurationHelper.getDockerParameters(),
                    PrometheusConfigurationHelper.getDockerVolumes(configLocation, internalLocation),
                    CommandConfigurationHelper.getDockerNetwork(network),
                    CommandConfigurationHelper.getDockerName(name),
                    image,
                    PrometheusConfigurationHelper.getDockerCommandArguments(port),
                    PrometheusConfigurationHelper.getDockerCommandOptions());
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