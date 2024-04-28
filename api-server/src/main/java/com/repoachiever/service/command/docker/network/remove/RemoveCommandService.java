package com.repoachiever.service.command.docker.network.remove;

import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Docker network removal command.
 */
public class RemoveCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public RemoveCommandService(String networkName) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format("docker network rm %s", networkName);
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
