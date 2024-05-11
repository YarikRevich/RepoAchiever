package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever API Server is already running.
 */
public class ApiServerInstanceIsAlreadyRunningException extends IOException {
    public ApiServerInstanceIsAlreadyRunningException() {
        this("");
    }

    public ApiServerInstanceIsAlreadyRunningException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever API Server instance is already running: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
