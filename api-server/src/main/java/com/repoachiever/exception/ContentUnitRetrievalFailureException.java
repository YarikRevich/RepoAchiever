package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content unit retrieval operation fails.
 */
public class ContentUnitRetrievalFailureException extends IOException {
    public ContentUnitRetrievalFailureException() {
        this("");
    }

    public ContentUnitRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content unit retrieval operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}