package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ApplicationFontFileNotFoundException extends IOException {
  public ApplicationFontFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format(
                "Application font file at the given location is not available: %s",
                Arrays.stream(message).toArray())
            .toString());
  }
}
