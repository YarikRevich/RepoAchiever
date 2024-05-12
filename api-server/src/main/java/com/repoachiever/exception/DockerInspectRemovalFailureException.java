package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when Docker inspection removal process fails.
 */
public class DockerInspectRemovalFailureException extends IOException {
    public DockerInspectRemovalFailureException() {
        this("");
    }

    public DockerInspectRemovalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Docker inspect container removal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
