package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class WorkspaceUnitInternalConfigFileNotFoundException extends IOException {
  public WorkspaceUnitInternalConfigFileNotFoundException() {
    this("");
  }

  public WorkspaceUnitInternalConfigFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format(
                "Workspace unit internal config file is not found: %s",
                Arrays.stream(message).toArray())
            .toString());
  }
}
