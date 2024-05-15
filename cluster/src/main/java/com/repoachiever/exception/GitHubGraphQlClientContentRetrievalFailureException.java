package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when GitHub GraphQL client content retrieval process fails.
 */
public class GitHubGraphQlClientContentRetrievalFailureException extends IOException {
    public GitHubGraphQlClientContentRetrievalFailureException() {
        this("");
    }

    public GitHubGraphQlClientContentRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("GitHub GraphQL client content retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
