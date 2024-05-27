package com.repoachiever.exception;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when smart graph css file was not found.
 */
public class SmartGraphCssFileNotFoundException extends FileNotFoundException {
  public SmartGraphCssFileNotFoundException(Object... message) {
    super(
            new Formatter()
                    .format("SmartGraph CSS file at the given location is not found: %s",
                            Arrays.stream(message).toArray())
                    .toString());
  }
}
