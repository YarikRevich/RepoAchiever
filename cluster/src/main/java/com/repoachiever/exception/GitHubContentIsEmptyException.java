package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when GitHub client content retrieval returns empty result.
 */
public class GitHubContentIsEmptyException extends IOException {
    public GitHubContentIsEmptyException() {
        this("");
    }

    public GitHubContentIsEmptyException(Object... message) {
        super(
                new Formatter()
                        .format("GitHub content result is empty: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
