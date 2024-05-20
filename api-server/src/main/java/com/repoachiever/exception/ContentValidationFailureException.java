package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content validation operation fails.
 */
public class ContentValidationFailureException extends IOException {
    public ContentValidationFailureException() {
        this("");
    }

    public ContentValidationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content validation operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}