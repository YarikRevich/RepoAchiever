package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class WorkspaceUnitDirectoryRemovalFailureException extends IOException {
    public WorkspaceUnitDirectoryRemovalFailureException() {
        this("");
    }

    public WorkspaceUnitDirectoryRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Workspace unit directory removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}