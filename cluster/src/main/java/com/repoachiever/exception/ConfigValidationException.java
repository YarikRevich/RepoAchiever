package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used to indicate configuration validation failure.
 */
public class ConfigValidationException extends IOException {
  public ConfigValidationException(Object... message) {
    super(
        new Formatter()
            .format("Config file content is not valid: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
