package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when given location definitions are not valid.
 */
public class LocationDefinitionsAreNotValidException extends IOException {
    public LocationDefinitionsAreNotValidException() {
        this("");
    }

    public LocationDefinitionsAreNotValidException(Object... message) {
        super(
                new Formatter()
                        .format("Location definitions are not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
