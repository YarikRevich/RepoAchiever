package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster application received timeout.
 */
public class ClusterApplicationTimeoutException extends IOException {
    public ClusterApplicationTimeoutException() {
        this("");
    }

    public ClusterApplicationTimeoutException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster application received timeout: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
