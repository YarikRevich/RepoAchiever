package com.repoachiever.resource.common;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.Provider;
import com.repoachiever.model.Exporter;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.hibernate.annotations.CompositeTypeRegistration;

import java.util.List;
import java.util.Objects;

/**
 * Contains helpful tools used for resource configuration.
 */
@ApplicationScoped
public class ResourceConfigurationHelper {
    @Inject
    PropertiesEntity properties;

    /**
     * Checks if the given export field is valid according to the used provider.
     *
     * @param provider given vendor provider.
     * @param exporter given exporter.
     * @return result of the check.
     */
    public Boolean isExporterFieldValid(Provider provider, Exporter exporter) {
        return switch (provider) {
            case EXPORTER -> Objects.nonNull(exporter);
            case GIT_GITHUB -> true;
        };
    }

    /**
     * Checks if the given external credentials field is valid according to the used provider.
     *
     * @param provider given vendor provider.
     * @param credentialsFieldExternal given credentials field.
     * @return result of the check.
     */
    public Boolean isExternalCredentialsFieldValid(
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
    public Boolean isLocationsDuplicate(List<String> locations) {
        return locations.stream().distinct().count() == locations.size();
    }

    /**
     * Checks if the given location definitions are valid according to the used provider.
     *
     * @param provider given vendor provider.
     * @param locations given locations.
     * @return result of the check.
     */
    public Boolean areLocationDefinitionsValid(Provider provider, List<String> locations) {
        return switch (provider) {
            case EXPORTER -> null;
            case GIT_GITHUB -> locations
                    .stream()
                    .allMatch(element -> element.matches(properties.getGitHubLocationNotation()));
        };
    }
}
