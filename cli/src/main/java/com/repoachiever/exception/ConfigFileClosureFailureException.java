package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when configuration file closure operation fails.
 */
public class ConfigFileClosureFailureException extends IOException {
    public ConfigFileClosureFailureException() {
        this("");
    }

    public ConfigFileClosureFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Config file closure operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}