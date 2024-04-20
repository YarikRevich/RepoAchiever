package com.repoachiever.api;

import com.repoachiever.model.ContentResult;
import java.io.File;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/content")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-04-20T13:09:08.079025+02:00[Europe/Warsaw]")
public interface ContentResourceApi {

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream" })
    File v1ContentDownloadGet(@QueryParam("id")   Integer id);

    @GET
    @Produces({ "application/json" })
    ContentResult v1ContentGet();
}
