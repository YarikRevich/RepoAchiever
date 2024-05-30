package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when configuration file is not found.
 */
public class ConfigFileNotFoundException extends IOException {
    public ConfigFileNotFoundException() {
        this("");
    }

    public ConfigFileNotFoundException(Object... message) {
        super(
                new Formatter()
                        .format("Config file is not found: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}