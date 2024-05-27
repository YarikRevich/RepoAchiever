package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content directory removal fails.
 */
public class ContentDirectoryRemovalFailureException extends IOException {
    public ContentDirectoryRemovalFailureException() {
        this("");
    }

    public ContentDirectoryRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content directory removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
