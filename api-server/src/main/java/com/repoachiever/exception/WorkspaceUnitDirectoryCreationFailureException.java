package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class WorkspaceUnitDirectoryCreationFailureException extends IOException {
    public WorkspaceUnitDirectoryCreationFailureException() {
        this("");
    }

    public WorkspaceUnitDirectoryCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Workspace unit directory creation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}