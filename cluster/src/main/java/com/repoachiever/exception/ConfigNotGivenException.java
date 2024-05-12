package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when config file is not provided.
 */
public class ConfigNotGivenException extends IOException {
    public ConfigNotGivenException() {
        this("");
    }

    public ConfigNotGivenException(Object... message) {
        super(
                new Formatter()
                        .format("Config file is not given: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}