package com.repoachiever.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

/** Represents context given for ResourceTracker Agent process. */
@Getter
public class ConfigEntity {
  /** Represents requests, which need to be executed. */
  @Getter
  public static class Request {
    @NotBlank public String name;

    @NotBlank public String script;

    @NotBlank public String frequency;
  }

  @Valid @NotNull public List<Request> requests;
}
