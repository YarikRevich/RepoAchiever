package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when files amount retrieval fails.
 */
public class ContentFilesAmountRetrievalFailureException extends IOException {
    public ContentFilesAmountRetrievalFailureException() {
        this("");
    }

    public ContentFilesAmountRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Files amount retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}