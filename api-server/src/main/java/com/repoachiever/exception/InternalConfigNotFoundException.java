package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class InternalConfigNotFoundException extends IOException {
  public InternalConfigNotFoundException() {
    this("");
  }

  public InternalConfigNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("Internal config file is not found: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
