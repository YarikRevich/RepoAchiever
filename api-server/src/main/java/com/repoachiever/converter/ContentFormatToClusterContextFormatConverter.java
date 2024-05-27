package com.repoachiever.converter;

import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.entity.common.ConfigEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents RepoAchiever Cluster content format to context format converter.
 */
public class ContentFormatToClusterContextFormatConverter {

    /**
     * Converts given content format to context format.
     *
     * @param contentFormat given content format to be converted.
     * @return converted context format.
     */
    public static ClusterContextEntity.Content.Format convert(ConfigEntity.Content.Format contentFormat) {
        return ClusterContextEntity.Content.Format.valueOf(
                Arrays.stream(
                                ClusterContextEntity.Content.Format.values())
                        .toList()
                        .stream()
                        .filter(element -> Objects.equals(element.toString(), contentFormat.toString()))
                        .map(Enum::name)
                        .toList()
                        .get(0));
    }
}