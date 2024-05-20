package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content removal operation fails.
 */
public class ContentRemovalFailureException extends IOException {
    public ContentRemovalFailureException() {
        this("");
    }

    public ContentRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content removal operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}