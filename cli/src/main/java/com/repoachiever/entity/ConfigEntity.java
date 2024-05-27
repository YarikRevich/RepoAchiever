package com.repoachiever.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import lombok.Getter;

/**
 * Service used to perform RepoAchiever CLI processing operation.
 */
@Getter
public class ConfigEntity {
    /**
     * Represents RepoAchiever Cluster configuration used for content management.
     */
    @Getter
    public static class Content {
        /**
         * Represents RepoAchiever Cluster configuration used for locations management.
         */
        @Getter
        public static class Location {
            @NotNull
            @JsonProperty("name")
            public String name;

            @NotNull
            @JsonProperty("additional")
            public Boolean additional;
        }

        @NotNull
        @JsonProperty("locations")
        public List<Location> locations;
    }

    @Valid
    @NotNull
    @JsonProperty("content")
    public Content content;

    /**
     * Represents external service configurations for RepoAchiever Cluster allocation used to retrieve content.
     */
    @Getter
    public static class Service {
        /**
         * Represents all supported service providers, which can be used by RepoAchiever Cluster allocation.
         */
        public enum Provider {
            @JsonProperty("exporter")
            EXPORTER("exporter"),

            @JsonProperty("git-github")
            GIT_GITHUB("git-github");

            private final String value;

            Provider(String value) {
                this.value = value;
            }

            public String toString() {
                return value;
            }
        }

        @JsonProperty("provider")
        public Provider provider;

        /**
         * Represents configuration used for communication with RepoAchiever Exporter.
         */
        @Getter
        public static class Exporter {
            @JsonProperty("host")
            public String host;
        }

        @JsonProperty("exporter")
        public Exporter exporter;

        /**
         * Represents credentials used for external service communication by RepoAchiever Cluster allocation.
         */
        @Getter
        public static class Credentials {
            @JsonProperty("id")
            public Integer id;

            @JsonProperty("token")
            public String token;
        }

        @JsonProperty("credentials")
        public Credentials credentials;
    }

    @Valid
    @NotNull
    @JsonProperty("service")
    public Service service;

    /**
     * Represents RepoAchiever API Server configuration used for further connection establishment.
     */
    @Getter
    public static class APIServer {
        @NotBlank
        public String host;
    }

    @Valid
    @NotNull
    @JsonProperty("api-server")
    public APIServer apiServer;
}
