package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents response returned by GitHub GraphQL client 'DefaultBranch' query.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitHubDefaultBranchResponseDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DefaultBranchRef {
        @JsonProperty("name")
        public String name;
    }

    @JsonProperty("defaultBranchRef")
    public DefaultBranchRef defaultBranchRef;
}
