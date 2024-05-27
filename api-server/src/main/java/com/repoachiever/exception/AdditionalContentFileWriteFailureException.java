package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content file write operation fails.
 */
public class AdditionalContentFileWriteFailureException extends IOException {
  public AdditionalContentFileWriteFailureException() {
    this("");
  }

  public AdditionalContentFileWriteFailureException(Object... message) {
    super(
        new Formatter()
            .format("Additional content file write operation failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
