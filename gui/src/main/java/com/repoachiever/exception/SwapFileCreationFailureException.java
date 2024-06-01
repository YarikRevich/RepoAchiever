package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception, when swap file creation operation failed.
 */
public class SwapFileCreationFailureException extends IOException {
    public SwapFileCreationFailureException() {
        this("");
    }

    public SwapFileCreationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Swap file creation operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
