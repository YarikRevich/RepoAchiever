package com.repoachiever.repository.common;

import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.Provider;
import java.util.Objects;
import java.util.Optional;

/**
 * Contains helpful tools used for repository configuration.
 */
public class RepositoryConfigurationHelper {
    /**
     * Extracts external credentials from the given credentials field as optional .
     *
     * @param provider given vendor provider.
     * @param credentialsFieldExternal given credentials field.
     * @return extracted external credentials as optional.
     */
    public static Optional<String> getExternalCredentials(
            Provider provider, CredentialsFieldsExternal credentialsFieldExternal) {
        return switch (provider) {
            case LOCAL -> Optional.empty();
            case GITHUB -> Objects.nonNull(credentialsFieldExternal.getToken());
        };
    }
}
