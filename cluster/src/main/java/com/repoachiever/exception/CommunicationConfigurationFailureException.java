package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when communication configuration fails.
 */
public class CommunicationConfigurationFailureException extends IOException {
    public CommunicationConfigurationFailureException() {
        this("");
    }

    public CommunicationConfigurationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Communication configuration operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
