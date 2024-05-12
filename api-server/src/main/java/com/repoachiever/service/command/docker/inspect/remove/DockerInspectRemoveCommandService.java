package com.repoachiever.service.command.docker.inspect.remove;

import jakarta.enterprise.context.ApplicationScoped;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Docker container removal command. Does nothing if given container does not exist.
 */
public class DockerInspectRemoveCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public DockerInspectRemoveCommandService(String name) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format("docker rm -f %s 2>/dev/null", name);
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
