package com.repoachiever.service.vendor;

import com.repoachiever.model.CredentialsFieldsExternal;
import com.repoachiever.model.Provider;
import com.repoachiever.service.vendor.git.github.GitGitHubVendorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** Provides high-level access to VCS vendor operations. */
@ApplicationScoped
public class VendorFacade {
    @Inject
    GitGitHubVendorService gitGitHubVendorService;

    /**
     * Checks if the given external credentials are valid, according to the given provider name.
     *
     * @param provider given external provider name.
     * @param credentialsFieldExternal given external credentials.
     * @return result of the check.
     */
    public Boolean isExternalCredentialsValid(Provider provider, CredentialsFieldsExternal credentialsFieldExternal) {
        return switch (provider) {
            case LOCAL -> true;
            case GITHUB -> gitGitHubVendorService.isTokenValid(credentialsFieldExternal.getToken());
        };
    }
}
