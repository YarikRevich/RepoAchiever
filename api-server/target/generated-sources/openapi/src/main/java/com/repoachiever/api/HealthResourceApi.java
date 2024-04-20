package com.repoachiever.api;

import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.ReadinessCheckApplication;
import com.repoachiever.model.ReadinessCheckResult;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-20T13:09:08.079025+02:00[Europe/Warsaw]")
public interface HealthResourceApi {

    @GET
    @Path("/health")
    @Produces({ "application/json" })
    HealthCheckResult v1HealthGet();

    @POST
    @Path("/readiness")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ReadinessCheckResult v1ReadinessPost(@Valid @NotNull ReadinessCheckApplication readinessCheckApplication);
}
