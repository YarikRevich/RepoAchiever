package com.repoachiever.entity.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents context sent to RepoAchiever Cluster as a variable during startup operation.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ClusterContextEntity {
    /**
     * Contains metadata for a specific RepoAchiever Cluster allocation.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Metadata {
        @JsonProperty("name")
        public String name;

        @JsonProperty("workspace_unit_key")
        public String workspaceUnitKey;
    }

    @JsonProperty("metadata")
    public Metadata metadata;

    /**
     * Represents RepoAchiever Cluster configuration used for content management.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Content {
        @JsonProperty("locations")
        public List<String> locations;

        @JsonProperty("format")
        public String format;
    }

    @JsonProperty("content")
    public Content content;

    /**
     * Represents external service configurations for RepoAchiever Cluster allocation used to retrieve content.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Service {
        /**
         * Represents all supported service providers, which can be used by RepoAchiever Cluster allocation.
         */
        public enum Provider {
            EXPORTER("exporter"),
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
        @AllArgsConstructor(staticName = "of")
        public static class Exporter {
            @JsonProperty("host")
            public String host;
        }

        @JsonProperty("exporter")
        public Exporter exporter;

        /**
         * Represents credentials used for external service communication by RepoAchiever Cluster allocation.
         */
        @AllArgsConstructor(staticName = "of")
        public static class Credentials {
            @JsonProperty("token")
            public String token;
        }

        @JsonProperty("credentials")
        public Credentials credentials;
    }

    @JsonProperty("service")
    public Service service;

    /**
     * Represents RepoAchiever Cluster configuration used for internal communication infrastructure setup.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Communication {
        @NotNull
        @JsonProperty("api_server_name")
        public String apiServerName;

        @JsonProperty("port")
        public Integer port;
    }

    @JsonProperty("communication")
    public Communication communication;

    /**
     * Represents RepoAchiever API Server resources configuration section.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Resource {
        /**
         * Represents RepoAchiever API Server configuration used for RepoAchiever Worker.
         */
        @AllArgsConstructor(staticName = "of")
        public static class Worker {
            @JsonProperty("frequency")
            public String frequency;
        }

        @JsonProperty("worker")
        public Worker worker;
    }

    @JsonProperty("resource")
    public Resource resource;
}