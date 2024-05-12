package com.repoachiever.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.Formatter;

public class CronExpressionException extends IOException {
  public CronExpressionException(Object... message) {
    super(
        new Formatter()
            .format("Invalid cron exception: %s", Arrays.stream(message).toArray())
            .toString());
  }
}
