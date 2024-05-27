package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content availability retrieval process fails.
 */
public class ContentAvailabilityRetrievalFailureException extends IOException {
    public ContentAvailabilityRetrievalFailureException() {
        this("");
    }

    public ContentAvailabilityRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content availability retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
