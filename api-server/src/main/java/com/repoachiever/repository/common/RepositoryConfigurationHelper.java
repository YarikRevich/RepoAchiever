package com.repoachiever.repository.common;

import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.CredentialsFieldsInternal;
import com.repoachiever.model.Provider;
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
            case EXPORTER -> Optional.empty();
            case GIT_GITHUB -> Optional.ofNullable(credentialsFieldExternal.getToken());
        };
    }

    /**
     * Converts given raw provider to content provider.
     *
     * @param value given raw provider.
     * @return converted content provider.
     */
    public static Provider convertRawProviderToContentProvider(String value) {
        return Provider.fromString(value);
    }

    /**
     * Converts given raw secrets to common credentials according to the given provider.
     *
     * @param provider given provider.
     * @param session given session identificator.
     * @param credentials given raw credentials.
     * @return converted common credentials.
     */
    public static CredentialsFieldsFull convertRawSecretsToContentCredentials(
            Provider provider, Integer session, Optional<String> credentials) {
        return switch (provider) {
            case EXPORTER -> CredentialsFieldsFull.of(
                    CredentialsFieldsInternal.of(session),
                    null);
            case GIT_GITHUB -> CredentialsFieldsFull.of(
                    CredentialsFieldsInternal.of(session),
                    CredentialsFieldsExternal.of(credentials.get()));
        };
    }
}
