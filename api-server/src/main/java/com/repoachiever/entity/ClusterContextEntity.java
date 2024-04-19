package com.repoachiever.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents context sent to RepoAchiever Cluster as a variable during startup operation. */
@Getter
@AllArgsConstructor(staticName = "of")
public class ClusterContextEntity {
  @Valid
  @NotNull
  @JsonProperty("name")
  private String name;
}
