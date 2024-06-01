package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when default configuration file processing fails.
 */
public class DefaultConfigurationFailureException extends IOException {
    public DefaultConfigurationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Default configuration file processing failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}