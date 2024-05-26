package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever API Server is not available.
 */
public class ApiServerNotAvailableException extends IOException {
  public ApiServerNotAvailableException() {
    this("");
  }

  public ApiServerNotAvailableException(Object... message) {
    super(
        new Formatter()
            .format("RepoAchiever API Server is not available: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
