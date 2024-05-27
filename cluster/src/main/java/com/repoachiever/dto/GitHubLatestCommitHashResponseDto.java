package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents response returned by GitHub GraphQL client 'LatestCommitHash' query.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitHubLatestCommitHashResponseDto {
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
                @Getter
                @Setter
                @AllArgsConstructor
                @NoArgsConstructor
                public static class Node {
                    @JsonProperty("oid")
                    public String oid;
                }

                @JsonProperty("nodes")
                public List<Node> nodes;
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