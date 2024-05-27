package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when Json to GitHub pull requests conversion operation fails.
 */
public class JsonToGitHubPullRequestsFailureException extends IOException {
    public JsonToGitHubPullRequestsFailureException() {
        this("");
    }

    public JsonToGitHubPullRequestsFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Json to GitHub pull requests conversion failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}