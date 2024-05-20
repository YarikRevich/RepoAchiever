package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when given content location is not valid.
 */
public class ContentLocationIsNotValidException extends IOException {
    public ContentLocationIsNotValidException() {
        this("");
    }

    public ContentLocationIsNotValidException(Object... message) {
        super(
                new Formatter()
                        .format("Content location is not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
