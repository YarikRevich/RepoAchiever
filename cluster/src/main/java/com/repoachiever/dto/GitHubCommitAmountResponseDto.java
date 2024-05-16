package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents response returned by GitHub GraphQL client 'CommitAmount' query.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitHubCommitAmountResponseDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Ref {
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Target {
            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            public static class History {
                @JsonProperty("totalCount")
                private Integer totalCount;
            }

            @JsonProperty("history")
            public History history;
        }

        @JsonProperty("target")
        public Target target;
    }

    @JsonProperty("ref")
    public Ref ref;
}

