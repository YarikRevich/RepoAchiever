package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster deployment fails.
 */
public class ClusterContentRetrievalFailureException extends IOException {
    public ClusterContentRetrievalFailureException() {
        this("");
    }

    public ClusterContentRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster content retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
