package com.repoachiever.dto;

import lombok.AllArgsConstructor;

/** Represents visualization label body. */
@AllArgsConstructor(staticName = "of")
public class VisualizationLabelDto {
  private final String message;

  private final Integer percentage;

  @Override
  public String toString() {
    int filledLength = (int) Math.round((double) percentage / 100 * 20);
    int emptyLength = 20 - filledLength;

    return "["
        + "#".repeat(Math.max(0, filledLength))
        + "-".repeat(Math.max(0, emptyLength))
        + "] "
        + percentage
        + "%"
        + " "
        + message;
  }
}
