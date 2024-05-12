package com.repoachiever.service.command.nodeexporter;

import com.repoachiever.service.command.nodeexporter.common.NodeExporterConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Prometheus Node Exporter deployment command.
 */
public class NodeExporterDeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public NodeExporterDeployCommandService(String name, String image, Integer port) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker run -d %s %s --name %s %s %s",
                    NodeExporterConfigurationHelper.getDockerVolumes(),
                    NodeExporterConfigurationHelper.getDockerPorts(port),
                    name,
                    image,
                    NodeExporterConfigurationHelper.getDockerCommandArguments());
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
