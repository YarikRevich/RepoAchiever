package com.repoachiever.api;

import com.repoachiever.model.LocationRegistrationApplication;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/registrator/location")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-19T11:12:35.929414+02:00[Europe/Warsaw]")
public interface RegistratorResourceApi {

    @POST
    @Consumes({ "application/json" })
    void v1RegistratorLocationPost(@Valid @NotNull LocationRegistrationApplication locationRegistrationApplication);
}
