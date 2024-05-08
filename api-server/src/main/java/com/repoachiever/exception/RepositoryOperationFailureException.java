package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when repository operation fails.
 */
public class RepositoryOperationFailureException extends IOException {
    public RepositoryOperationFailureException() {
        this("");
    }

    public RepositoryOperationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Repository operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}