package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when raw content creation operation fails.
 */
public class RawContentCreationFailureException extends IOException {
    public RawContentCreationFailureException() {
        this("");
    }

    public RawContentCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Raw content creation operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}