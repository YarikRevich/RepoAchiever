package com.repoachiever.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Service used to perform RepoAchiever Cluster processing operation.
 */
@Getter
public class ConfigEntity {
    /**
     * Contains metadata for a specific RepoAchiever Cluster allocation.
     */
    @Getter
    public static class Metadata {
        @JsonProperty("name")
        public String name;

        @JsonProperty("workspace_unit_key")
        public String workspaceUnitKey;
    }

    @Valid
    @NotNull
    @JsonProperty("metadata")
    public Metadata metadata;

    /** Represents RepoAchiever Cluster configuration used for content management. */
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

        @NotNull
        @Pattern(regexp = "(^zip$)|(^tar$)")
        @JsonProperty("format")
        public String format;
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

    /** Represents RepoAchiever Cluster configuration used for internal communication infrastructure setup. */
    @Getter
    public static class Communication {
        @NotNull
        @JsonProperty("api_server_name")
        public String apiServerName;

        @NotNull
        @JsonProperty("port")
        public Integer port;
    }

    @Valid
    @NotNull
    @JsonProperty("communication")
    public Communication communication;

    /**
     * Represents RepoAchiever API Server resources configuration section.
     */
    @Getter
    public static class Resource {
        /**
         * Represents RepoAchiever API Server configuration used for RepoAchiever Worker.
         */
        @Getter
        public static class Worker {
            @NotNull
            @Pattern(
                    regexp =
                            "(((([0-9]|[0-5][0-9])(-([0-9]|[0-5][0-9]))?,)*([0-9]|[0-5][0-9])(-([0-9]|[0-5][0-9]))?)|(([\\*]|[0-9]|[0-5][0-9])/([0-9]|[0-5][0-9]))|([\\?])|([\\*]))[\\s](((([0-9]|[0-5][0-9])(-([0-9]|[0-5][0-9]))?,)*([0-9]|[0-5][0-9])(-([0-9]|[0-5][0-9]))?)|(([\\*]|[0-9]|[0-5][0-9])/([0-9]|[0-5][0-9]))|([\\?])|([\\*]))[\\s](((([0-9]|[0-1][0-9]|[2][0-3])(-([0-9]|[0-1][0-9]|[2][0-3]))?,)*([0-9]|[0-1][0-9]|[2][0-3])(-([0-9]|[0-1][0-9]|[2][0-3]))?)|(([\\*]|[0-9]|[0-1][0-9]|[2][0-3])/([0-9]|[0-1][0-9]|[2][0-3]))|([\\?])|([\\*]))[\\s](((([1-9]|[0][1-9]|[1-2][0-9]|[3][0-1])(-([1-9]|[0][1-9]|[1-2][0-9]|[3][0-1]))?,)*([1-9]|[0][1-9]|[1-2][0-9]|[3][0-1])(-([1-9]|[0][1-9]|[1-2][0-9]|[3][0-1]))?(C)?)|(([1-9]|[0][1-9]|[1-2][0-9]|[3][0-1])/([1-9]|[0][1-9]|[1-2][0-9]|[3][0-1])(C)?)|(L(-[0-9])?)|(L(-[1-2][0-9])?)|(L(-[3][0-1])?)|(LW)|([1-9]W)|([1-3][0-9]W)|([\\?])|([\\*]))[\\s](((([1-9]|0[1-9]|1[0-2])(-([1-9]|0[1-9]|1[0-2]))?,)*([1-9]|0[1-9]|1[0-2])(-([1-9]|0[1-9]|1[0-2]))?)|(([1-9]|0[1-9]|1[0-2])/([1-9]|0[1-9]|1[0-2]))|(((JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(-(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?,)*(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(-(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)|((JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)/(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))|([\\?])|([\\*]))[\\s]((([1-7](-([1-7]))?,)*([1-7])(-([1-7]))?)|([1-7]/([1-7]))|(((MON|TUE|WED|THU|FRI|SAT|SUN)(-(MON|TUE|WED|THU|FRI|SAT|SUN))?,)*(MON|TUE|WED|THU|FRI|SAT|SUN)(-(MON|TUE|WED|THU|FRI|SAT|SUN))?(C)?)|((MON|TUE|WED|THU|FRI|SAT|SUN)/(MON|TUE|WED|THU|FRI|SAT|SUN)(C)?)|(([1-7]|(MON|TUE|WED|THU|FRI|SAT|SUN))?(L|LW)?)|(([1-7]|MON|TUE|WED|THU|FRI|SAT|SUN)#([1-7])?)|([\\?])|([\\*]))([\\s]?(([\\*])?|(19[7-9][0-9])|(20[0-9][0-9]))?|"
                                    + " (((19[7-9][0-9])|(20[0-9][0-9]))/((19[7-9][0-9])|(20[0-9][0-9])))?|"
                                    + " ((((19[7-9][0-9])|(20[0-9][0-9]))(-((19[7-9][0-9])|(20[0-9][0-9])))?,)*((19[7-9][0-9])|(20[0-9][0-9]))(-((19[7-9][0-9])|(20[0-9][0-9])))?)?)")
            @JsonProperty("frequency")
            public String frequency;
        }

        @Valid
        @NotNull
        @JsonProperty("worker")
        public Worker worker;
    }

    @Valid
    @NotNull
    @JsonProperty("resource")
    public Resource resource;
}
