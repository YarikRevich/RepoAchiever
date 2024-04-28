package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class DockerIsNotAvailableException extends IOException {
    public DockerIsNotAvailableException() {
        this("");
    }

    public DockerIsNotAvailableException(Object... message) {
        super(
                new Formatter()
                        .format("Docker instance is not available: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
