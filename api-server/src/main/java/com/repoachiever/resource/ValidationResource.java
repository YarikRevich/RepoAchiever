package com.repoachiever.resource;

import com.repoachiever.api.ValidationResourceApi;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.ValidationSecretsApplication;
import com.repoachiever.model.ValidationSecretsApplicationResult;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;

/** Contains implementation of ValidationResource. */
@ApplicationScoped
public class ValidationResource implements ValidationResourceApi {
  @Inject
  VendorFacade vendorFacade;

  /**
   * Implementation for declared in OpenAPI configuration v1SecretsAcquirePost method.
   *
   * @param validationSecretsApplication application used for secrets validation.
   * @return Secrets validation result.
   */
  @Override
  @SneakyThrows
  public ValidationSecretsApplicationResult v1SecretsAcquirePost(
      ValidationSecretsApplication validationSecretsApplication) {
      return vendorFacade.isTokenValid(validationSecretsApplication);
  }
}
