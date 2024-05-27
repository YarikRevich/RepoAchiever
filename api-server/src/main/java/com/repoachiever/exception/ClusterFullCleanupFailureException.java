package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster full cleanup fails.
 */
public class ClusterFullCleanupFailureException extends IOException {
    public ClusterFullCleanupFailureException() {
        this("");
    }

    public ClusterFullCleanupFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster full cleanup failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
