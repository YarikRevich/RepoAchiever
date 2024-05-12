package com.repoachiever.service.command.docker.network.remove;

import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Docker network removal command.
 */
public class DockerNetworkRemoveCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public DockerNetworkRemoveCommandService(String networkName) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format("docker network rm %s 2> /dev/null", networkName);
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
