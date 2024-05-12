package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/** Represents exception, when swap file deletion failed. */
public class SwapFileDeletionFailedException extends IOException {
  public SwapFileDeletionFailedException() {
    this("");
  }

  public SwapFileDeletionFailedException(Object... message) {
    super(
        new Formatter()
            .format("Swap file deletion failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
