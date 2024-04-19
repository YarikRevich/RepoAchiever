package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ApplicationImageFileNotFoundException extends IOException {
  public ApplicationImageFileNotFoundException() {
    this("");
  }

  public ApplicationImageFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format(
                "Application image file at the given location is not available: %s",
                Arrays.stream(message).toArray())
            .toString());
  }
}
