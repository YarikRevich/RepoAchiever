package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when running state file creation fails.
 */
public class RunningStateFileCreationFailureException extends IOException {
    public RunningStateFileCreationFailureException() {
        this("");
    }

    public RunningStateFileCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Running state file creation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}