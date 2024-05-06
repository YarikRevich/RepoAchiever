package com.repoachiever.converter;

import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.model.Provider;

/**
 * Represents RepoAchiever Cluster content provider to context provider converter.
 */
public class ContentProviderToClusterContextProviderConverter {
    public static ClusterContextEntity.Service.Provider convert(Provider contentProvider) {
        return ClusterContextEntity.Service.Provider.valueOf(contentProvider.toString());
    }
}
