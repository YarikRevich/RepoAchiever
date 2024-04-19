package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/** Represents exception, when given file is not valid. */
public class ScriptDataValidationException extends IOException {
  public ScriptDataValidationException() {
    this("");
  }

  public ScriptDataValidationException(Object... message) {
    super(
        new Formatter()
            .format("Given explicit script file is not valid: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
