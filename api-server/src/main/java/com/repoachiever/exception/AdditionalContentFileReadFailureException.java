package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content file read operation fails.
 */
public class AdditionalContentFileReadFailureException extends IOException {
    public AdditionalContentFileReadFailureException() {
        this("");
    }

    public AdditionalContentFileReadFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Additional content file read operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
