package com.repoachiever.service.command.cluster.deploy;

import com.repoachiever.service.command.cluster.common.ClusterConfigurationHelper;
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

    public DeployCommandService(
            String clusterContext, String binDirectory, String binClusterLocation) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "%s java -jar %s & echo $!",
                    ClusterConfigurationHelper.getEnvironmentVariables(clusterContext),
                    Path.of(binDirectory, binClusterLocation));
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
