package com.repoachiever.exception;

import java.io.FileNotFoundException;
import java.util.Formatter;

public class SmartGraphPropertiesFileNotFoundException extends FileNotFoundException {
  public SmartGraphPropertiesFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("SmartGraph properties file at the given location is not available", message)
            .toString());
  }
}
