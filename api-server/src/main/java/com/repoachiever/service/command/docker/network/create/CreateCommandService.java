package com.repoachiever.service.command.docker.network.create;

import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Docker network creation command.
 */
public class CreateCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public CreateCommandService(String networkName) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker network inspect %s >/dev/null 2>&1 || docker network create -d bridge %s",
                    networkName,
                    networkName);
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
