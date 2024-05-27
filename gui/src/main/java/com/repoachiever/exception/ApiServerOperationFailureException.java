package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever API Server operation fails.
 */
public class ApiServerOperationFailureException extends IOException {
  public ApiServerOperationFailureException() {
    this("");
  }

  public ApiServerOperationFailureException(Object... message) {
    super(
            new Formatter()
                    .format("RepoAchiever API Server operation failed: %s", Arrays.stream(message).toArray())
                    .toString());
  }
}

