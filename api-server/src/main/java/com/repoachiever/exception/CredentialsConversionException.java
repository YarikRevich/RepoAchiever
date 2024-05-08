package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when credentials conversion process fails.
 */
public class CredentialsConversionException extends IOException {
  public CredentialsConversionException(Object... message) {
    super(
        new Formatter()
            .format("Given credentials are invalid: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
