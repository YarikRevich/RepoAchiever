package com.repoachiever.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

/**
 * Represents configuration model used for RepoAchiever API Server operations.
 */
@Getter
@ApplicationScoped
public class ConfigEntity {
    /**
     * Represents RepoAchiever API Server configuration used for RepoAchiever API Server instance setup.
     */
    @Getter
    public static class Connection {
        @NotNull
        @JsonProperty("port")
        public Integer port;
    }

    @Valid
    @NotNull
    @JsonProperty("connection")
    public Connection connection;

    /**
     * Represents RepoAchiever API Server configuration used for internal communication infrastructure setup.
     */
    @Getter
    public static class Communication {
        @NotNull
        @JsonProperty("port")
        public Integer port;
    }

    @Valid
    @NotNull
    @JsonProperty("communication")
    public Communication communication;

    /**
     * Represents RepoAchiever API Server configuration used for content management.
     */
    @Getter
    public static class Content {
        /**
         * Represents all supported content formats, which can be used by RepoAchiever Cluster allocation.
         */
        @Getter
        public enum Format {
            @JsonProperty("zip")
            ZIP("zip"),

            @JsonProperty("tar")
            TAR("tar");

            private final String value;

            Format(String value) {
                this.value = value;
            }

            public String toString() {
                return value;
            }
        }

        @Valid
        @NotNull
        @JsonProperty("format")
        public Format format;
    }

    @Valid
    @NotNull
    @JsonProperty("content")
    public Content content;

    /**
     * Represents RepoAchiever API Server configuration used for diagnostics.
     */
    @Getter
    public static class Diagnostics {
        @NotNull
        @JsonProperty("enabled")
        public Boolean enabled;

        /**
         * Represents RepoAchiever API Server metrics configuration setup.
         */
        @Getter
        public static class Metrics {
            @NotNull
            @JsonProperty("port")
            public Integer port;
        }

        @Valid
        @NotNull
        @JsonProperty("metrics")
        public Metrics metrics;

        /**
         * Represents RepoAchiever API Server configuration used for Grafana instance setup.
         */
        @Getter
        public static class Grafana {
            @NotNull
            @JsonProperty("port")
            public Integer port;
        }

        @Valid
        @NotNull
        @JsonProperty("grafana")
        public Grafana grafana;

        /**
         * Represents RepoAchiever API Server configuration used for Prometheus instance setup.
         */
        @Getter
        public static class Prometheus {
            @NotNull
            @JsonProperty("port")
            public Integer port;
        }

        @Valid
        @NotNull
        @JsonProperty("prometheus")
        public Prometheus prometheus;

        /**
         * Represents RepoAchiever API Server configuration used for Prometheus Node Exporter instance setup.
         */
        @Getter
        public static class NodeExporter {
            @NotNull
            @JsonProperty("port")
            public Integer port;
        }

        @Valid
        @NotNull
        @JsonProperty("node-exporter")
        public NodeExporter nodeExporter;
    }

    @Valid
    @NotNull
    @JsonProperty("diagnostics")
    public Diagnostics diagnostics;

    /**
     * Represents RepoAchiever API Server resources configuration section.
     */
    @Getter
    public static class Resource {
        /**
         * Represents RepoAchiever API Server configuration used for RepoAchiever Cluster.
         */
        @Getter
        public static class Cluster {
            @NotNull
            @Min(1)
            @JsonProperty("max-workers")
            public Integer maxWorkers;

            @NotNull
            @Min(1)
            @JsonProperty("max-versions")
            public Integer maxVersions;
        }

        @Valid
        @NotNull
        @JsonProperty("cluster")
        public Cluster cluster;

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