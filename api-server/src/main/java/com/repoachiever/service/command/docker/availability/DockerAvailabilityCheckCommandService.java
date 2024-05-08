package com.repoachiever.service.command.docker.availability;

import jakarta.enterprise.context.ApplicationScoped;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Docker availability check command.
 */
@ApplicationScoped
public class DockerAvailabilityCheckCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public DockerAvailabilityCheckCommandService() {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
                    case WINDOWS -> null;
                    case UNIX, MAC, ANY -> "docker ps 2>/dev/null";
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
