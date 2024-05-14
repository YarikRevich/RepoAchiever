package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when exporter field is not valid.
 */
public class ExporterFieldIsNotValidException extends IOException {
    public ExporterFieldIsNotValidException() {
        this("");
    }

    public ExporterFieldIsNotValidException(Object... message) {
        super(
                new Formatter()
                        .format("Exporter field is not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}