package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when workspace unit directory is already present.
 */
public class WorkspaceUnitDirectoryPresentException extends IOException {
  public WorkspaceUnitDirectoryPresentException() {
    this("");
  }

  public WorkspaceUnitDirectoryPresentException(Object... message) {
    super(
        new Formatter()
            .format(
                "Workspace unit is already present, please stop current deployment first: %s",
                Arrays.stream(message).toArray())
            .toString());
  }
}
