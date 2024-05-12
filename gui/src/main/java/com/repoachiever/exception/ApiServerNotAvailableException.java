package com.repoachiever.exception;

import java.io.IOException;
import java.util.Formatter;

public class ApiServerNotAvailableException extends IOException {
  public ApiServerNotAvailableException(Object... message) {
    super(new Formatter().format("API Server is not available", message).toString());
  }
}
