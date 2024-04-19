package com.repoachiever.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents script validation application used for script acquiring process. */
@Getter
@AllArgsConstructor(staticName = "of")
public class ValidationScriptApplicationDto {
  private List<String> fileContent;
}
