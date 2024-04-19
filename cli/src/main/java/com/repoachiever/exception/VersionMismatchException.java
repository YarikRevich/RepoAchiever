package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/** Represents exception, when API Server version is different from the version of the client. */
public class VersionMismatchException extends IOException {
  public VersionMismatchException() {
    this("");
  }

  public VersionMismatchException(Object... message) {
    super(
        new Formatter()
            .format(
                "API Server version is different from the version of the client: %s",
                Arrays.stream(message).toArray())
            .toString());
  }
}
