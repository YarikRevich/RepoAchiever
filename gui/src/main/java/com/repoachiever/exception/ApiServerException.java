package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ApiServerException extends IOException {
  public ApiServerException() {
    this("");
  }

  public ApiServerException(Object... message) {
    super(
        new Formatter()
            .format("API Server exception: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
