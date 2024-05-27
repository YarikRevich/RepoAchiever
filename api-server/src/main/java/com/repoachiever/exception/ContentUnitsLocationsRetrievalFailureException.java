package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content units locations retrieval operation fails.
 */
public class ContentUnitsLocationsRetrievalFailureException extends IOException {
    public ContentUnitsLocationsRetrievalFailureException() {
        this("");
    }

    public ContentUnitsLocationsRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content units locations retrieval operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}