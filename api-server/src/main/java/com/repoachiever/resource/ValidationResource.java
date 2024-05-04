package com.repoachiever.resource;

import com.repoachiever.api.ValidationResourceApi;
import com.repoachiever.exception.CredentialsFieldIsNotValidException;
import com.repoachiever.model.ValidationSecretsApplication;
import com.repoachiever.model.ValidationSecretsApplicationResult;
import com.repoachiever.resource.common.ResourceConfigurationHelper;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;

/**
 * Contains implementation of ValidationResource.
 */
@ApplicationScoped
public class ValidationResource implements ValidationResourceApi {
    @Inject
    VendorFacade vendorFacade;

    /**
     * Implementation for declared in OpenAPI configuration v1SecretsAcquirePost method.
     *
     * @param validationSecretsApplication application used for secrets validation.
     * @return secrets validation result.
     */
    @Override
    @SneakyThrows
    public ValidationSecretsApplicationResult v1ValidationSecretsPost(
            ValidationSecretsApplication validationSecretsApplication) {
        if (!ResourceConfigurationHelper.isCredentialsFieldValid(
                validationSecretsApplication.getProvider(), validationSecretsApplication.getCredentials())) {
            throw new CredentialsFieldIsNotValidException();
        }

        return vendorFacade.isTokenValid(validationSecretsApplication);
    }
}
