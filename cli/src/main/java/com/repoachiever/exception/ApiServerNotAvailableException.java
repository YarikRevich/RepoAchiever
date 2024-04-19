package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ApiServerNotAvailableException extends IOException {
  public ApiServerNotAvailableException() {
    this("");
  }

  public ApiServerNotAvailableException(Object... message) {
    super(
        new Formatter()
            .format("API Server is not available: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
