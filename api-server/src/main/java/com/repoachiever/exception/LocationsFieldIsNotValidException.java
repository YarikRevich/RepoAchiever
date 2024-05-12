package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when locations field is not valid.
 */
public class LocationsFieldIsNotValidException extends IOException {
    public LocationsFieldIsNotValidException() {
        this("");
    }

    public LocationsFieldIsNotValidException(Object... message) {
        super(
                new Formatter()
                        .format("Locations field is not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}