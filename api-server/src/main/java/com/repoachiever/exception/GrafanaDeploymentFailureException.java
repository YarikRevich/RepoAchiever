package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when Grafana deployment operation fails.
 */
public class GrafanaDeploymentFailureException extends IOException {
    public GrafanaDeploymentFailureException() {
        this("");
    }

    public GrafanaDeploymentFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Grafana deployment failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}