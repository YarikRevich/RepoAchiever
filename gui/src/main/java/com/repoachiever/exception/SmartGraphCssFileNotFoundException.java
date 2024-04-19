package com.repoachiever.exception;

import java.io.FileNotFoundException;
import java.util.Formatter;

public class SmartGraphCssFileNotFoundException extends FileNotFoundException {
  public SmartGraphCssFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("SmartGraph CSS file at the given location is not available", message)
            .toString());
  }
}
