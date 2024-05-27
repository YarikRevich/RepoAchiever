package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when files locations retrieval fails.
 */
public class ContentFilesLocationsRetrievalFailureException extends IOException {
    public ContentFilesLocationsRetrievalFailureException() {
        this("");
    }

    public ContentFilesLocationsRetrievalFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Files locations retrieval failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}