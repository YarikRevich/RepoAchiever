package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Represents exception used when configuration file cron expression validation
 * process fails.
 */
public class ConfigCronExpressionValidationException extends IOException {
    public ConfigCronExpressionValidationException() {
        this("");
    }

    public ConfigCronExpressionValidationException(Object... message) {
        super(
                new Formatter()
                        .format("Config file cron expression is not valid: %s", Arrays.stream(message).toArray())
                        .toString());
    }
}
