package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Represents retrieved GitHub user details. 
 */
@Getter
@ApplicationScoped
public class GitHubUserDto {
    /**
     * Represents user name.
     */
    @Valid
    @NotNull
    @JsonProperty("login")
    public String login;
}