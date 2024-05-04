package com.repoachiever.api;

import com.repoachiever.model.ContentApplication;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentRetrievalResult;
import java.io.File;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/content")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-04T11:08:42.730835+02:00[Europe/Warsaw]")
public interface ContentResourceApi {

    @POST
    @Path("/apply")
    @Consumes({ "application/json" })
    void v1ContentApplyPost(@Valid @NotNull ContentApplication contentApplication);

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream" })
    File v1ContentDownloadGet(@QueryParam("location")   String location);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ContentRetrievalResult v1ContentPost(@Valid @NotNull ContentRetrievalApplication contentRetrievalApplication);
}
