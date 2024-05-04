package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ClusterOperationFailureException extends IOException {
    public ClusterOperationFailureException() {
        this("");
    }

    public ClusterOperationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
