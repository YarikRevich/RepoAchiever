package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when database query execution operation fails.
 */
public class QueryExecutionFailureException extends IOException {
    public QueryExecutionFailureException() {
        this("");
    }

    public QueryExecutionFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Query execution failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
