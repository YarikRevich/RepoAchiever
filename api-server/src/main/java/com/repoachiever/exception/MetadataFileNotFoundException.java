package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when metadata file was not found.
 */
public class MetadataFileNotFoundException extends IOException {
  public MetadataFileNotFoundException() {
    this("");
  }

  public MetadataFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format("Metadata file is not found: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
