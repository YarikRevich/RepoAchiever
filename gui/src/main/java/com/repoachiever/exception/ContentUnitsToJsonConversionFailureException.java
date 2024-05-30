package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content units to Json conversion operation fails.
 */
public class ContentUnitsToJsonConversionFailureException extends IOException {
    public ContentUnitsToJsonConversionFailureException() {
        this("");
    }

    public ContentUnitsToJsonConversionFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content units to Json conversion operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}