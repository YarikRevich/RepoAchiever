package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class CloudCredentialsValidationException extends IOException {
  public CloudCredentialsValidationException() {
    this("");
  }

  public CloudCredentialsValidationException(Object... message) {
    super(
        new Formatter()
            .format("Given cloud credentials are not valid!: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
