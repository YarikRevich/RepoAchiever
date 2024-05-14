package com.repoachiever.resource.common;

import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.Provider;

import java.util.List;
import java.util.Objects;

/**
 * Contains helpful tools used for resource configuration.
 */
public class ResourceConfigurationHelper {
    /**
     * Checks if the given external credentials field is valid according to the used provider.
     *
     * @param provider given vendor provider.
     * @param credentialsFieldExternal given credentials field.
     * @return result of the check.
     */
    public static Boolean isExternalCredentialsFieldValid(
            Provider provider, CredentialsFieldsExternal credentialsFieldExternal) {
        return switch (provider) {
            case EXPORTER -> true;
            case GIT_GITHUB -> Objects.nonNull(credentialsFieldExternal);
        };
    }

    /**
     * Checks if the given locations have duplicates.
     *
     * @param locations given locations.
     * @return result of the check.
     */
    public static Boolean isLocationsDuplicate(List<String> locations) {
        return locations.stream().distinct().count() == locations.size();
    }
}
