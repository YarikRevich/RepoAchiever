package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class ClusterDestructionFailureException extends IOException {
    public ClusterDestructionFailureException() {
        this("");
    }

    public ClusterDestructionFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster destruction failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
