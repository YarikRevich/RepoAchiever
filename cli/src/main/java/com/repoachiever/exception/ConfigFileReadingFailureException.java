package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when configuration file reading operation fails.
 */
public class ConfigFileReadingFailureException extends IOException {
    public ConfigFileReadingFailureException() {
        this("");
    }

    public ConfigFileReadingFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Config file reading operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}