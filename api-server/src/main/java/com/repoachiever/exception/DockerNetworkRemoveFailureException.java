package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class DockerNetworkRemoveFailureException extends IOException {
    public DockerNetworkRemoveFailureException() {
        this("");
    }

    public DockerNetworkRemoveFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Docker network removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}