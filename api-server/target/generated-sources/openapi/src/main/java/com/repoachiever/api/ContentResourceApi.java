package com.repoachiever.api;

import com.repoachiever.model.ContentApplication;
import com.repoachiever.model.ContentCleanup;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.model.ContentWithdrawal;
import java.io.File;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/content")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-17T01:46:26.799650+02:00[Europe/Warsaw]")
public interface ContentResourceApi {

    @POST
    @Path("/apply")
    @Consumes({ "application/json" })
    void v1ContentApplyPost(@Valid @NotNull ContentApplication contentApplication);

    @POST
    @Path("/clean")
    @Consumes({ "application/json" })
    void v1ContentCleanPost(@Valid @NotNull ContentCleanup contentCleanup);

    @GET
    @Path("/download")
    @Produces({ "application/octet-stream" })
    File v1ContentDownloadGet(@QueryParam("location")   String location);

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    ContentRetrievalResult v1ContentPost(@Valid @NotNull ContentRetrievalApplication contentRetrievalApplication);

    @DELETE
    @Path("/withdraw")
    @Consumes({ "application/json" })
    void v1ContentWithdrawDelete(@Valid @NotNull ContentWithdrawal contentWithdrawal);
}
