package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when raw content files amount retrieval fails.
 */
public class RawContentFilesAmountRetrievalFailureException extends IOException {
    public RawContentFilesAmountRetrievalFailureException() {
        this("");
    }

    public RawContentFilesAmountRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Raw content files amount retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}