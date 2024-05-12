package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when config file is not valid.
 */
public class ConfigValidationException extends IOException {
  public ConfigValidationException() {
    this("");
  }

  public ConfigValidationException(Object... message) {
    super(
        new Formatter()
            .format("Config file content is not valid: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
