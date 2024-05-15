package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents response returned by GitHub GraphQL client 'CommitAmount' query. */
@Getter
public class GitHubCommitAmountResponseDto {
    @Getter
    public static class Data {
        @Getter
        public static class Repository {
            @Getter
            public static class Ref {
                @Getter
                public static class Target {
                    @Getter
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

        @JsonProperty("repository")
        public Repository repository;
    }

    @JsonProperty("data")
    public Data data;
}

