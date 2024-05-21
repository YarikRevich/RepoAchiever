package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster content availability retrieval operation fails.
 */
public class ClusterContentAvailabilityRetrievalFailureException extends IOException {
    public ClusterContentAvailabilityRetrievalFailureException() {
        this("");
    }

    public ClusterContentAvailabilityRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster content availability retrieval operation failed: %s",
                                Arrays.stream(message).toArray())
                        .toString());
    }
}
