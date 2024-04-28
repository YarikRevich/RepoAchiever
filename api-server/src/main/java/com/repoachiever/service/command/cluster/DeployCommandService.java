package com.repoachiever.service.command.cluster;

import jakarta.enterprise.context.ApplicationScoped;
import process.SProcess;
import process.SProcessExecutor;

import java.nio.file.Path;

/**
 * Represents RepoAchiever Cluster deployment command.
 */
public class DeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public DeployCommandService(String binDirectory, String binClusterName) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "java -jar %s &",
                    Path.of(binDirectory, binClusterName));
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
