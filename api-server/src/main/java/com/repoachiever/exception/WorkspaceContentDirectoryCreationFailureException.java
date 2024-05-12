package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when workspace content directory creation operation fails.
 */
public class WorkspaceContentDirectoryCreationFailureException extends IOException {
    public WorkspaceContentDirectoryCreationFailureException() {
        this("");
    }

    public WorkspaceContentDirectoryCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Workspace content directory creation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}