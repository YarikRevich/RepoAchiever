package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content creation operation fails.
 */
public class AdditionalContentCreationFailureException extends IOException {
    public AdditionalContentCreationFailureException() {
        this("");
    }

    public AdditionalContentCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Additional content creation operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}