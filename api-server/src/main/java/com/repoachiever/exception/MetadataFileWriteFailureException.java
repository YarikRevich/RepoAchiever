package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class MetadataFileWriteFailureException extends IOException {
  public MetadataFileWriteFailureException() {
    this("");
  }

  public MetadataFileWriteFailureException(Object... message) {
    super(
        new Formatter()
            .format("Metadata file write failed: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
