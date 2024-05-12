package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when content application retrieval process fails.
 */
public class ContentApplicationRetrievalFailureException extends IOException {
    public ContentApplicationRetrievalFailureException() {
        this("");
    }

    public ContentApplicationRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Content application retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
