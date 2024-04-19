package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents gathered output of the executed command. */
@Getter
@AllArgsConstructor(staticName = "of")
public class CommandExecutorOutputDto {
  private String normalOutput;

  private String errorOutput;
}
