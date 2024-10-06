package com.repoachiever.service.command.cluster.destroy;

import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents RepoAchiever Cluster destruction command.
 */
public class ClusterDestroyCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public ClusterDestroyCommandService(Integer pid) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format("kill -9 %d", pid);
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
