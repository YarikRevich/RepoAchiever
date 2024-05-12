package com.repoachiever.api;

import com.repoachiever.model.ClusterInfoUnit;
import com.repoachiever.model.VersionInfoResult;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;



import java.io.InputStream;
import java.util.Map;
import java.util.List;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

@Path("/v1/info")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-05-12T23:08:54.088125+02:00[Europe/Warsaw]")
public interface InfoResourceApi {

    @GET
    @Path("/cluster")
    @Produces({ "application/json" })
    List<ClusterInfoUnit> v1InfoClusterGet();

    @GET
    @Path("/telemetry")
    @Produces({ "text/plain" })
    String v1InfoTelemetryGet();

    @GET
    @Path("/version")
    @Produces({ "application/json" })
    VersionInfoResult v1InfoVersionGet();
}
