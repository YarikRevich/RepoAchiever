package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when RepoAchiever Cluster withdrawal fails.
 */
public class ClusterWithdrawalFailureException extends IOException {
    public ClusterWithdrawalFailureException() {
        this("");
    }

    public ClusterWithdrawalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("RepoAchiever Cluster withdrawal failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
