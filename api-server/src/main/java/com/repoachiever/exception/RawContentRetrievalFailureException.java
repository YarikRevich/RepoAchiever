package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when raw content retrieval operation fails.
 */
public class RawContentRetrievalFailureException extends IOException {
    public RawContentRetrievalFailureException() {
        this("");
    }

    public RawContentRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Raw content retrieval operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
