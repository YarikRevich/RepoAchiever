package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class CredentialsConversionException extends IOException {
  public CredentialsConversionException(Object... message) {
    super(
        new Formatter()
            .format("Given credentials are invalid: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
