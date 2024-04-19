package com.repoachiever.service.client.grafana;

import com.repoachiever.dto.GrafanaDashboardImportDto;
import com.repoachiever.model.HealthCheckResult;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/** Represents client for grafana remote API. */
@Path("/api")
@RegisterRestClient(configKey = "grafana")
public interface IGrafanaClientService {
    @POST
    @Path("/dashboards/import")
    @Consumes(MediaType.APPLICATION_JSON)
    void apiDashboardsImport(GrafanaDashboardImportDto grafanaDashboardImport);
}
