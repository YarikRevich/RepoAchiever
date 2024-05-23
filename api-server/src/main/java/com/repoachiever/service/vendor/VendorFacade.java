package com.repoachiever.service.vendor;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.Provider;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import com.repoachiever.service.vendor.git.github.GitGitHubVendorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Provides high-level access to VCS vendor operations.
 */
@ApplicationScoped
public class VendorFacade {
    @Inject
    PropertiesEntity properties;

    @Inject
    VendorConfigurationHelper vendorConfigurationHelper;

    @Inject
    GitGitHubVendorService gitGitHubVendorService;

    /**
     * Checks if provided vendor provider is available.
     *
     * @return result of the check.
     */
    public Boolean isVendorAvailable(Provider provider) {
        return switch (provider) {
            case EXPORTER -> null;
            case GIT_GITHUB -> vendorConfigurationHelper.isVendorAvailable(properties.getRestClientGitHubUrl());
        };
    }

    /**
     * Checks if the given external credentials are valid, according to the given provider name.
     *
     * @param provider                 given external provider name.
     * @param credentialsFieldExternal given external credentials.
     * @return result of the check.
     */
    public Boolean isExternalCredentialsValid(Provider provider, CredentialsFieldsExternal credentialsFieldExternal) {
        return switch (provider) {
            case EXPORTER -> true;
            case GIT_GITHUB -> gitGitHubVendorService.isTokenValid(
                    vendorConfigurationHelper.getWrappedToken(credentialsFieldExternal.getToken()));
        };
    }

    /**
     * Checks if the given location are valid.
     *
     * @param provider                 given external provider name.
     * @param credentialsFieldExternal given external credentials.
     * @param locations                given location names.
     * @return result of the check.
     */
    public Boolean areLocationsValid(
            Provider provider, CredentialsFieldsExternal credentialsFieldExternal, List<String> locations) {
        return switch (provider) {
            case EXPORTER -> true;
            case GIT_GITHUB -> gitGitHubVendorService.areLocationsValid(
                    vendorConfigurationHelper.getWrappedToken(credentialsFieldExternal.getToken()),
                    locations
                            .stream()
                            .map(element -> vendorConfigurationHelper.parseLocationGitHubNotation(element))
                            .toList());
        };
    }
}
