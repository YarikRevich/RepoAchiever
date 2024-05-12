package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents version retrieval command result. */
@Getter
@AllArgsConstructor(staticName = "of")
public class VersionExternalCommandResultDto {
  private String data;

  private Boolean status;

  private String error;
}
