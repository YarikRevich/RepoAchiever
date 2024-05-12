package com.repoachiever.dto;

import com.repoachiever.model.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents secrets validation application used for secrets acquiring process. */
@Getter
@AllArgsConstructor(staticName = "of")
public class ValidationSecretsApplicationDto {
  private Provider provider;

  private String filePath;
}
