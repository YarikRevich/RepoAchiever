package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents stop deployment command result. */
@Getter
@AllArgsConstructor(staticName = "of")
public class StopExternalCommandResultDto {
  private Boolean status;

  private String error;
}
