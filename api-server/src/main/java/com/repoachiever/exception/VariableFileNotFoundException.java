package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class VariableFileNotFoundException extends IOException {
  public VariableFileNotFoundException() {
    this("");
  }

  public VariableFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("Variable file is not found: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
