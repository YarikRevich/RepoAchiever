package com.repoachiever.repository.common;

import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.CredentialsFieldsInternal;
import com.repoachiever.model.Provider;
import com.repoachiever.model.Exporter;
import java.util.Optional;

/**
 * Contains helpful tools used for repository configuration.
 */
public class RepositoryConfigurationHelper {
    /**
     * Extracts external credentials from the given credentials field as optional.
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
     * Extracts export field as optional.
     *
     * @param provider given vendor provider.
     * @param exporter given exporter field.
     * @return extracted exporter as optional.
     */
    public static Optional<Exporter> getExporter(Provider provider, Exporter exporter) {
        return switch (provider) {
            case EXPORTER -> Optional.ofNullable(exporter);
            case GIT_GITHUB -> Optional.empty();
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
     * Converts given raw exporter to content exporter.
     *
     * @param host given exporter host.
     * @return converted content exporter.
     */
    public static Exporter convertRawExporterToContentExporter(String host) {
        return Exporter.of(host);
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
