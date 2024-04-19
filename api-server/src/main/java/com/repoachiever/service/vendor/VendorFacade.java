package com.repoachiever.service.vendor;

import com.repoachiever.model.ValidationSecretsApplication;
import com.repoachiever.model.ValidationSecretsApplicationResult;
import com.repoachiever.service.vendor.git.github.GitGitHubVendorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** Provides high-level access to VCS vendor operations. */
@ApplicationScoped
public class VendorFacade {
    @Inject
    GitGitHubVendorService gitGitHubVendorService;

    /**
     * Checks if the given token is valid.
     *
     * @param validationSecretsApplication given secrets validation application.
     * @return result of the check.
     */
    public ValidationSecretsApplicationResult isTokenValid(ValidationSecretsApplication validationSecretsApplication) {
        return switch (validationSecretsApplication.getProvider()) {
            case LOCAL -> ValidationSecretsApplicationResult.of(true);
            case GITHUB -> ValidationSecretsApplicationResult.of(
                    gitGitHubVendorService.isTokenValid(validationSecretsApplication.getCredentials().getToken()));
        };
    }
}
