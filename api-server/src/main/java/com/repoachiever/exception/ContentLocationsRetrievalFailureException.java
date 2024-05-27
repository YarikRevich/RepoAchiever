package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content reference creation fails.
 */
public class ContentLocationsRetrievalFailureException extends IOException {
    public ContentLocationsRetrievalFailureException() {
        this("");
    }

    public ContentLocationsRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content locations retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
