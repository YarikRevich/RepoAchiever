package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class SecretsConversionException extends IOException {
  public SecretsConversionException(Object... message) {
    super(
        new Formatter()
            .format("Given secrets are invalid: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
