package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster recreation operation fails.
 */
public class ClusterRecreationFailureException extends IOException {
    public ClusterRecreationFailureException() {
        this("");
    }

    public ClusterRecreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster recreation operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
