package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ContentFileWriteFailureException extends IOException {
    public ContentFileWriteFailureException() {
        this("");
    }

    public ContentFileWriteFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content file write failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
