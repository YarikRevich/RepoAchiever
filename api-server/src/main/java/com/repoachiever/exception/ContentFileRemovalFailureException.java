package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when file removal operation fails.
 */
public class ContentFileRemovalFailureException extends IOException {
    public ContentFileRemovalFailureException() {
        this("");
    }

    public ContentFileRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("File removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}