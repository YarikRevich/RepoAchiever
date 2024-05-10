package com.repoachiever.converter;

import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.model.Provider;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents RepoAchiever Cluster content provider to context provider converter.
 */
public class ContentProviderToClusterContextProviderConverter {
    public static ClusterContextEntity.Service.Provider convert(Provider contentProvider) {
        return ClusterContextEntity.Service.Provider.valueOf(
                Arrays.stream(
                                ClusterContextEntity.Service.Provider.values())
                        .toList()
                        .stream()
                        .filter(element -> Objects.equals(element.toString(), contentProvider.toString()))
                        .map(Enum::name)
                        .toList()
                        .get(0));
    }
}
