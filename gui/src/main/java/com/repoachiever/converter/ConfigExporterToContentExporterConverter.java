package com.repoachiever.converter;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.model.Exporter;

/**
 * Represents config exporter to RepoAchiever API Server content exporter converter.
 */
public class ConfigExporterToContentExporterConverter {

    /**
     * Converts given config exporter to content exporter.
     *
     * @param provider given config provider.
     * @param exporter given config exporter to be converted.
     * @return converted content exporter.
     */
    public static Exporter convert(ConfigEntity.Service.Provider provider, ConfigEntity.Service.Exporter exporter) {
        return switch (provider) {
            case EXPORTER -> Exporter.of(exporter.getHost());
            case GIT_GITHUB -> null;
        };
    }
}