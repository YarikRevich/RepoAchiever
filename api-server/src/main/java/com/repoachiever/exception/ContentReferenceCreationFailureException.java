package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content reference creation fails.
 */
public class ContentReferenceCreationFailureException extends IOException {
    public ContentReferenceCreationFailureException() {
        this("");
    }

    public ContentReferenceCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content reference creation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
