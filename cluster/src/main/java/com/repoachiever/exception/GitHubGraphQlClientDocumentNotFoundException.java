package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when GitHub GraphQL client document file is not found.
 */
public class GitHubGraphQlClientDocumentNotFoundException extends IOException {
    public GitHubGraphQlClientDocumentNotFoundException() {
        this("");
    }

    public GitHubGraphQlClientDocumentNotFoundException(Object... message) {
        super(
                new Formatter()
                        .format("GitHub GraphQL client document file not found: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
