package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents start deployment command result. */
@Getter
@AllArgsConstructor(staticName = "of")
public class StartExternalCommandResultDto {
  private Boolean status;

  private String error;
}
