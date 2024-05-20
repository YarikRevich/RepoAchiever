package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster content reference retrieval operation fails.
 */
public class ClusterContentReferenceRetrievalFailureException extends IOException {
    public ClusterContentReferenceRetrievalFailureException() {
        this("");
    }

    public ClusterContentReferenceRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster content reference retrieval operation failed: %s",
                                Arrays.stream(message).toArray())
                        .toString());
    }
}
