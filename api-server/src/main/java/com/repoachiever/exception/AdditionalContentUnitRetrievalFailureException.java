package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content  retrieval fails.
 */
public class AdditionalContentUnitRetrievalFailureException extends IOException {
    public AdditionalContentUnitRetrievalFailureException() {
        this("");
    }

    public AdditionalContentUnitRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Additional content unit retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
