package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ClusterUnhealthyReapplicationFailureException extends IOException {
    public ClusterUnhealthyReapplicationFailureException() {
        this("");
    }

    public ClusterUnhealthyReapplicationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster unhealthy allocation repplication failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
