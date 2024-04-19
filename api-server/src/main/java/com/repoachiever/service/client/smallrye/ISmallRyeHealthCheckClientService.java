package com.repoachiever.service.client.smallrye;

import com.repoachiever.model.HealthCheckResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/** Represents client for SmallRye health check endpoints. */
@Path("/q")
@RegisterRestClient(configKey = "small-rye-health-check")
public interface ISmallRyeHealthCheckClientService {
  @GET
  @Path("/health")
  @Produces(MediaType.APPLICATION_JSON)
  HealthCheckResult qHealthGet();
}
