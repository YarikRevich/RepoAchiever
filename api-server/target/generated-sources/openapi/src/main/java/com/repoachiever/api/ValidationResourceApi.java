package com.repoachiever.api;

import com.repoachiever.model.ValidationSecretsApplication;
import com.repoachiever.model.ValidationSecretsApplicationResult;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/validation/secrets")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-20T13:09:08.079025+02:00[Europe/Warsaw]")
public interface ValidationResourceApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ValidationSecretsApplicationResult v1ValidationSecretsPost(@Valid @NotNull ValidationSecretsApplication validationSecretsApplication);
}
