package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when Json to additional content data conversion operation fails.
 */
public class JsonToAdditionalContentDataFailureException extends IOException {
    public JsonToAdditionalContentDataFailureException() {
        this("");
    }

    public JsonToAdditionalContentDataFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Json to additional content data conversion failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}