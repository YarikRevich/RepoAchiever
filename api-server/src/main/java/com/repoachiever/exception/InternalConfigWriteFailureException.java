package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class InternalConfigWriteFailureException extends IOException {
  public InternalConfigWriteFailureException() {
    this("");
  }

  public InternalConfigWriteFailureException(Object... message) {
    super(
        new Formatter()
            .format("Internal config file write failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
