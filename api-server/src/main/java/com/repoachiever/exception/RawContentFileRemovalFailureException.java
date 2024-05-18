package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when raw content file removal operation fails.
 */
public class RawContentFileRemovalFailureException extends IOException {
    public RawContentFileRemovalFailureException() {
        this("");
    }

    public RawContentFileRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Raw content file removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}