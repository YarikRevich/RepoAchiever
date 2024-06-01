package com.repoachiever.converter;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.CredentialsFieldsInternal;

/**
 * Represents config credentials to RepoAchiever API Server content credentials converter.
 */
public class ConfigCredentialsToContentCredentialsConverter {

    /**
     * Converts given config credentials to context exporter.
     *
     * @param provider given config provider.
     * @param configCredentials given config credentials to be converted.
     * @return converted context exporter.
     */
    public static CredentialsFieldsFull convert(
            ConfigEntity.Service.Provider provider, ConfigEntity.Service.Credentials configCredentials) {
        return switch (provider) {
            case EXPORTER -> null;
            case GIT_GITHUB -> CredentialsFieldsFull.of(
                    CredentialsFieldsInternal.of(configCredentials.getId()),
                    CredentialsFieldsExternal.of(configCredentials.getToken()));
        };
    }
}