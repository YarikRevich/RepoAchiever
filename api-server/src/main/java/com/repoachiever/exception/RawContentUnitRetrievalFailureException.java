package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when raw content  retrieval fails.
 */
public class RawContentUnitRetrievalFailureException extends IOException {
    public RawContentUnitRetrievalFailureException() {
        this("");
    }

    public RawContentUnitRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Raw content unit retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
