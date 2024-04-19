package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents readiness check command result. */
@Getter
@AllArgsConstructor(staticName = "of")
public class ReadinessCheckInternalCommandResultDto {
  private Boolean status;

  private String error;
}
