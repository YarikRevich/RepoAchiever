package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/** Represents exception, when swap file creation failed. */
public class SwapFileCreationFailedException extends IOException {
  public SwapFileCreationFailedException() {
    this("");
  }

  public SwapFileCreationFailedException(Object... message) {
    super(
        new Formatter()
            .format("Swap file creation failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
