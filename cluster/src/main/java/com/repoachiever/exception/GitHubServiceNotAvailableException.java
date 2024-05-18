package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when GitHub external API service is not available.
 */
public class GitHubServiceNotAvailableException extends IOException {
    public GitHubServiceNotAvailableException() {
        this("");
    }

    public GitHubServiceNotAvailableException(Object... message) {
        super(
                new Formatter()
                        .format("GitHub external API service is not available: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
