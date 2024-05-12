package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster deployment fails.
 */
public class ClusterDeploymentFailureException extends IOException {
    public ClusterDeploymentFailureException() {
        this("");
    }

    public ClusterDeploymentFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster deployment failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
