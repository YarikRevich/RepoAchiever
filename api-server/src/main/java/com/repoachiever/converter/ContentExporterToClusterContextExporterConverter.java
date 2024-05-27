package com.repoachiever.converter;

import com.repoachiever.model.Provider;
import com.repoachiever.model.Exporter;

import com.repoachiever.entity.common.ClusterContextEntity;

/**
 * Represents RepoAchiever Cluster content exporter to context exporter converter.
 */
public class ContentExporterToClusterContextExporterConverter {

    /**
     * Converts given content exporter to context exporter.
     *
     * @param provider given content provider.
     * @param exporter given content exporter to be converted.
     * @return converted context exporter.
     */
    public static ClusterContextEntity.Service.Exporter convert(Provider provider, Exporter exporter) {
        return switch (provider) {
            case EXPORTER -> ClusterContextEntity.Service.Exporter.of(exporter.getHost());
            case GIT_GITHUB -> null;
        };
    }
}