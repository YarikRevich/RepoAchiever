package com.repoachiever.service.command.nodeexporter;

import com.repoachiever.service.command.common.CommandConfigurationHelper;
import com.repoachiever.service.command.nodeexporter.common.NodeExporterConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Prometheus Node Exporter deployment command.
 */
public class NodeExporterDeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public NodeExporterDeployCommandService(String name, String image, Integer port, String network) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker run -d %s %s %s %s %s",
                    NodeExporterConfigurationHelper.getDockerVolumes(),
                    CommandConfigurationHelper.getDockerNetwork(network),
                    CommandConfigurationHelper.getDockerName(name),
                    image,
                    NodeExporterConfigurationHelper.getDockerCommandArguments(port));
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
