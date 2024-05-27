package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when unit directory creation operation fails.
 */
public class UnitDirectoryCreationFailureException extends IOException {
    public UnitDirectoryCreationFailureException() {
        this("");
    }

    public UnitDirectoryCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Unit directory creation operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}