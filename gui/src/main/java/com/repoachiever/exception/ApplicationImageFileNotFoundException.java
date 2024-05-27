package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when application image file was not found.
 */
public class ApplicationImageFileNotFoundException extends IOException {
  public ApplicationImageFileNotFoundException() {
    this("");
  }

  public ApplicationImageFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format(
                "Application image file at the given location is not found: %s",
                Arrays.stream(message).toArray())
            .toString());
  }
}
