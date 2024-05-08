package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when Prometheus Node Exporter deployment operation fails.
 */
public class NodeExporterDeploymentFailureException extends IOException {
    public NodeExporterDeploymentFailureException() {
        this("");
    }

    public NodeExporterDeploymentFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Prometheus node exporter deployment failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
