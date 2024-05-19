package com.repoachiever.api;

import com.repoachiever.model.ContentStateApplication;
import com.repoachiever.model.ContentStateApplicationResult;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/state/content")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-19T15:06:43.082824+02:00[Europe/Warsaw]")
public interface StateResourceApi {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ContentStateApplicationResult v1StateContentPost(@Valid @NotNull ContentStateApplication contentStateApplication);
}
