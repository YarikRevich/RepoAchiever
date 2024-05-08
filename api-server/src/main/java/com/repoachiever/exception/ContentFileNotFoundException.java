package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when configuration file content was not found.
 */
public class ContentFileNotFoundException extends IOException {
    public ContentFileNotFoundException() {
        this("");
    }

    public ContentFileNotFoundException(Object... message) {
        super(
                new Formatter()
                        .format("Content file is not found: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
