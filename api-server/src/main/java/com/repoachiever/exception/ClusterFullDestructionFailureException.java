package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster full destruction fails.
 */
public class ClusterFullDestructionFailureException extends IOException {
    public ClusterFullDestructionFailureException() {
        this("");
    }

    public ClusterFullDestructionFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster full destruction failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
