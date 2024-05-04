package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class RepositoryApplicationFailureException extends IOException {
    public RepositoryApplicationFailureException() {
        this("");
    }

    public RepositoryApplicationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster repository application failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
