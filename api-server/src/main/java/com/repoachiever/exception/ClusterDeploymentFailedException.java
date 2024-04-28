package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ClusterDeploymentFailedException extends IOException {
    public ClusterDeploymentFailedException() {
        this("");
    }

    public ClusterDeploymentFailedException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster deployment failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
