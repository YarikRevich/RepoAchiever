package com.repoachiever.converter;

import com.repoachiever.model.Provider;
import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.entity.common.ClusterContextEntity;

/**
 * Represents RepoAchiever Cluster content credentials to context credentials converter.
 */
public class ContentCredentialsToClusterContextCredentialsConverter {
    public static ClusterContextEntity.Service.Credentials convert(
            Provider provider, CredentialsFieldsExternal credentialsFieldsExternal) {
        return switch (provider) {
            case LOCAL -> null;
            case GITHUB -> ClusterContextEntity.Service.Credentials.of(credentialsFieldsExternal.getToken());
        };
    }
}