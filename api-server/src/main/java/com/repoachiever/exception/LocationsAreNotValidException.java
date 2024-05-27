package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when given credentials are not valid.
 */
public class LocationsAreNotValidException extends IOException {
    public LocationsAreNotValidException() {
        this("");
    }

    public LocationsAreNotValidException(Object... message) {
        super(
                new Formatter()
                        .format("Locations are not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
