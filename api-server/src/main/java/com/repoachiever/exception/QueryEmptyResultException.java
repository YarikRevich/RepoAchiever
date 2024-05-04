package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class QueryEmptyResultException extends IOException {
    public QueryEmptyResultException() {
        this("");
    }

    public QueryEmptyResultException(Object... message) {
        super(
                new Formatter()
                        .format("Query result is empty: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
