package com.repoachiever.service.client.github;

import com.repoachiever.dto.GrafanaDashboardImportDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/** Represents client for GitHub remote API. */
@RegisterRestClient(configKey = "github")
public interface IGitHubClientService {
    @GET
    @Path("/octocat")
    @Produces(MediaType.APPLICATION_JSON)
    Response getOctocat(@HeaderParam("Authorization") String token);
}
