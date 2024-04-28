package com.repoachiever.service.client.github;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.HeaderParam;

/** Represents client for GitHub remote API. */
@RegisterRestClient(configKey = "github")
public interface IGitHubClientService {
    @GET
    @Path("/octocat")
    @Produces(MediaType.APPLICATION_JSON)
    Response getOctocat(@HeaderParam(HttpHeaders.AUTHORIZATION) String token);
}
