package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when raw content file write operation fails.
 */
public class RawContentFileWriteFailureException extends IOException {
    public RawContentFileWriteFailureException() {
        this("");
    }

    public RawContentFileWriteFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Raw content file write operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
