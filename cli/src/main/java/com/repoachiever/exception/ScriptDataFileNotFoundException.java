package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ScriptDataFileNotFoundException extends IOException {
  public ScriptDataFileNotFoundException() {
    this("");
  }

  public ScriptDataFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("Given explicit script file is not found: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
