package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when repository content application operation fails.
 */
public class RepositoryContentApplicationFailureException extends IOException {
    public RepositoryContentApplicationFailureException() {
        this("");
    }

    public RepositoryContentApplicationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster repository content application failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
