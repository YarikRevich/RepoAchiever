package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/** Represents exception, when swap file deletion operation failed. */
public class SwapFileDeletionFailureException extends IOException {
  public SwapFileDeletionFailureException() {
    this("");
  }

  public SwapFileDeletionFailureException(Object... message) {
    super(
        new Formatter()
            .format("Swap file deletion operation failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
