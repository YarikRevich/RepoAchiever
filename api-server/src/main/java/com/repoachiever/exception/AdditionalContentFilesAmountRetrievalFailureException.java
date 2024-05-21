package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when additional content files amount retrieval fails.
 */
public class AdditionalContentFilesAmountRetrievalFailureException extends IOException {
    public AdditionalContentFilesAmountRetrievalFailureException() {
        this("");
    }

    public AdditionalContentFilesAmountRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Additional content files amount retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}