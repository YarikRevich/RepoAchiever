package com.repoachiever.entity.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    }

    @JsonProperty("metadata")
    public Metadata metadata;

    /**
     * Represents filter section elected for a specific RepoAchiever Cluster allocation.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Filter {
        @JsonProperty("locations")
        public List<String> locations;
    }

    @JsonProperty("filter")
    public Filter filter;

    /**
     * Represents external service configurations for RepoAchiever Cluster allocation used to retrieve content.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Service {
        /**
         * Represents all supported service providers, which can be used by RepoAchiever Cluster allocation.
         */
        public enum Provider {
            LOCAL("git-local"),
            GITHUB("git-github");

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
        @JsonProperty("port")
        public Integer port;
    }

    @JsonProperty("communication")
    public Communication communication;

    /**
     * Represents RepoAchiever Cluster configuration used for content management.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Content {
        @JsonProperty("format")
        public String format;
    }

    @JsonProperty("content")
    public Content content;

    /**
     * Represents RepoAchiever API Server resources configuration section.
     */
    @AllArgsConstructor(staticName = "of")
    public static class Resource {
        /**
         * Represents RepoAchiever API Server configuration used for RepoAchiever Cluster.
         */
        @AllArgsConstructor(staticName = "of")
        public static class Cluster {
            @JsonProperty("max-workers")
            public Integer maxWorkers;
        }

        @JsonProperty("cluster")
        public Cluster cluster;

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