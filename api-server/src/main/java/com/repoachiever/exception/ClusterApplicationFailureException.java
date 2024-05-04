package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ClusterApplicationFailureException extends IOException {
    public ClusterApplicationFailureException() {
        this("");
    }

    public ClusterApplicationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster application failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
