package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when telemetry operation fails.
 */
public class TelemetryOperationFailureException extends IOException {
    public TelemetryOperationFailureException() {
        this("");
    }

    public TelemetryOperationFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Telemetry operation failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}