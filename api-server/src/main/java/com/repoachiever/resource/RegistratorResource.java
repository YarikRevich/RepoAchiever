package com.repoachiever.resource;

import com.repoachiever.api.RegistratorResourceApi;
import com.repoachiever.model.LocationRegistrationApplication;
import jakarta.enterprise.context.ApplicationScoped;

/** Contains implementation of RegistratorResource. */
@ApplicationScoped
public class RegistratorResource implements RegistratorResourceApi {
    /**
     * Implementation for declared in OpenAPI configuration v1RegistratorLocationPost method.
     *
     * @param locationRegistrationApplication application used to perform location registration.
     */
    @Override
    public void v1RegistratorLocationPost(LocationRegistrationApplication locationRegistrationApplication) {

    }
}
