package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when topology info units to Json conversion operation fails.
 */
public class TopologyInfoUnitsToJsonConversionFailureException extends IOException {
    public TopologyInfoUnitsToJsonConversionFailureException() {
        this("");
    }

    public TopologyInfoUnitsToJsonConversionFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Topology info units to Json conversion operation failed: %s",
                                Arrays.stream(message).toArray())
                        .toString());
    }
}