package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when Prometheus deployment operation fails.
 */
public class PrometheusDeploymentFailureException extends IOException {
    public PrometheusDeploymentFailureException() {
        this("");
    }

    public PrometheusDeploymentFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Prometheus deployment failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}