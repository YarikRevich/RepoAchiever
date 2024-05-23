package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when provider is not available.
 */
public class ProviderIsNotAvailableException extends IOException {
    public ProviderIsNotAvailableException() {
        this("");
    }

    public ProviderIsNotAvailableException(Object... message) {
        super(
                new Formatter()
                        .format("Provider is not available: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
