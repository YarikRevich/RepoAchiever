package com.repoachiever.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

/** Represents context given for ResourceTracker Agent process. */
@Getter
public class ConfigEntity {
  @Valid
  @NotNull
  private String name;
}
