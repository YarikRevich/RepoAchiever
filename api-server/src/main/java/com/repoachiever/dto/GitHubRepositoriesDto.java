package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Represents retrieved GitHub repository details. 
 */
@Getter
@ApplicationScoped
public class GitHubRepositoriesDto {
    /**
     * Represents remote repository short name.
     */
    @Valid
    @NotNull
    @JsonProperty("name")
    public String name;

    /**
     * Represents remote repository default branch.
     */
    @Valid
    @NotNull
    @JsonProperty("default_branch")
    public String defaultBranch;
}