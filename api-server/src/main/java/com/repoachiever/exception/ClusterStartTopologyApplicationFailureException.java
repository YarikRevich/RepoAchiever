package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever API Server start topology applications fails.
 */
public class ClusterStartTopologyApplicationFailureException extends IOException {
    public ClusterStartTopologyApplicationFailureException() {
        this("");
    }

    public ClusterStartTopologyApplicationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever API start topology application failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
