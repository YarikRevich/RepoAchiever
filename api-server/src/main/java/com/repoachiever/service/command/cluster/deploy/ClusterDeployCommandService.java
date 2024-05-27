package com.repoachiever.service.command.cluster.deploy;

import com.repoachiever.service.command.cluster.common.ClusterConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;

import java.nio.file.Path;

/**
 * Represents RepoAchiever Cluster deployment command.
 */
public class ClusterDeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public ClusterDeployCommandService(
            String clusterContext, String clusterBinLocation) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "%s java -jar %s & echo $!",
                    ClusterConfigurationHelper.getEnvironmentVariables(clusterContext),
                    Path.of(clusterBinLocation));
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
