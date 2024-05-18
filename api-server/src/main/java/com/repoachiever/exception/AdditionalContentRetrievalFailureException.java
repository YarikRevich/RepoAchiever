package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content retrieval operation fails.
 */
public class AdditionalContentRetrievalFailureException extends IOException {
    public AdditionalContentRetrievalFailureException() {
        this("");
    }

    public AdditionalContentRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Additional content retrieval operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
