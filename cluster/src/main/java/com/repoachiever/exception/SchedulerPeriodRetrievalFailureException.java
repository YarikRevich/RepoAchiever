package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when scheduler period retrieval process fails.
 */
public class SchedulerPeriodRetrievalFailureException extends IOException {
    public SchedulerPeriodRetrievalFailureException() {
        this("");
    }

    public SchedulerPeriodRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Config file content is not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
