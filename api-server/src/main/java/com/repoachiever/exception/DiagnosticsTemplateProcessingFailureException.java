package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class DiagnosticsTemplateProcessingFailureException extends IOException {
    public DiagnosticsTemplateProcessingFailureException() {
        this("");
    }

    public DiagnosticsTemplateProcessingFailureException(Object... message) {
        super(
                new Formatter()
                        .format("Diagnostics template processing failed: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
