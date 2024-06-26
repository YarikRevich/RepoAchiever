package com.repoachiever.converter;

import com.repoachiever.model.Provider;
import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.entity.common.ClusterContextEntity;

/**
 * Represents RepoAchiever Cluster content credentials to context credentials converter.
 */
public class ContentCredentialsToClusterContextCredentialsConverter {

    /**
     * Converts given content exporter to context exporter.
     *
     * @param provider given content provider.
     * @param credentialsFieldsExternal given content external credentials to be converted.
     * @return converted context exporter.
     */
    public static ClusterContextEntity.Service.Credentials convert(
            Provider provider, CredentialsFieldsExternal credentialsFieldsExternal) {
        return switch (provider) {
            case EXPORTER -> null;
            case GIT_GITHUB -> ClusterContextEntity.Service.Credentials.of(credentialsFieldsExternal.getToken());
        };
    }
}