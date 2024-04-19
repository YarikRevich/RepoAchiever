package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class VariableFileWriteFailureException extends IOException {
  public VariableFileWriteFailureException() {
    this("");
  }

  public VariableFileWriteFailureException(Object... message) {
    super(
        new Formatter()
            .format("Variable file write failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
