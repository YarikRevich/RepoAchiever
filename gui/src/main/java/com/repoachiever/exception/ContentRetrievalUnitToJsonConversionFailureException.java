package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content retrieval unit to Json conversion operation fails.
 */
public class ContentRetrievalUnitToJsonConversionFailureException extends IOException {
    public ContentRetrievalUnitToJsonConversionFailureException() {
        this("");
    }

    public ContentRetrievalUnitToJsonConversionFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content retrieval unit to Json conversion operation failed: %s",
                                Arrays.stream(message).toArray())
                        .toString());
    }
}