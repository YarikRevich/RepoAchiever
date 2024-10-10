package com.repoachiever.service.client.github;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getPermissionScopes(@HeaderParam(HttpHeaders.AUTHORIZATION) String token);

    @GET
    @Path("/user/repos")
    @Produces(MediaType.APPLICATION_JSON)
    Response getRepositories(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            @QueryParam("per_page") Integer perPage,
            @QueryParam("page") Integer page,
            @QueryParam("visibility") String visibility);

    @GET
    @Path("/repos/{owner}/{name}/branches/{branch}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getRepository(
            @HeaderParam(HttpHeaders.AUTHORIZATION) String token,
            @PathParam("owner") String owner,
            @PathParam("name") String name,
            @PathParam("branch") String branch);
}
