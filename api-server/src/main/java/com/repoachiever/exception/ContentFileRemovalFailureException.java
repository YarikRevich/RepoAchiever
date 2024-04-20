package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ContentFileRemovalFailureException extends IOException {
    public ContentFileRemovalFailureException() {
        this("");
    }

    public ContentFileRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content file removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}