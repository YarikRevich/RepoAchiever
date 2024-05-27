package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content file was not found.
 */
public class AdditionalContentFileNotFoundException extends IOException {
  public AdditionalContentFileNotFoundException() {
    this("");
  }

  public AdditionalContentFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("Additional content file is not found: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
