package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when GitHub GraphQL client content retrieval process fails.
 */
public class GitHubContentRetrievalFailureException extends IOException {
    public GitHubContentRetrievalFailureException() {
        this("");
    }

    public GitHubContentRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("GitHub content retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
