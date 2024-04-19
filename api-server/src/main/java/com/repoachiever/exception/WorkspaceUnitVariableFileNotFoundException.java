package com.repoachiever.exception;

import java.nio.file.NoSuchFileException;
import java.util.Arrays;
import java.util.Formatter;

public class WorkspaceUnitVariableFileNotFoundException extends NoSuchFileException {
  public WorkspaceUnitVariableFileNotFoundException() {
    this("");
  }

  public WorkspaceUnitVariableFileNotFoundException(Object... message) {
    super(
        new Formatter()
            .format(
                "Workspace unit variables file is not found: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
