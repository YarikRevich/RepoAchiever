package com.repoachiever.resource;

import com.repoachiever.api.HealthResourceApi;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.ReadinessCheckApplication;
import com.repoachiever.model.ReadinessCheckResult;
import com.repoachiever.service.client.smallrye.ISmallRyeHealthCheckClientService;
import com.repoachiever.service.workspace.WorkspaceService;
import com.repoachiever.service.workspace.facade.WorkspaceFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * Contains implementation of HealthResource.
 */
@ApplicationScoped
public class HealthResource implements HealthResourceApi {
    @Inject
    PropertiesEntity properties;

    @Inject
    WorkspaceFacade workspaceFacade;

    @Inject
    WorkspaceService workspaceService;

    @Inject
    @RestClient
    ISmallRyeHealthCheckClientService smallRyeHealthCheckClientService;

    /**
     * Implementation for declared in OpenAPI configuration v1HealthGet method.
     *
     * @return health check result.
     */
    @Override
    public HealthCheckResult v1HealthGet() {
        try {
            return smallRyeHealthCheckClientService.qHealthGet();
        } catch (WebApplicationException e) {
            return e.getResponse().readEntity(HealthCheckResult.class);
        }
    }

    /**
     * Implementation for declared in OpenAPI configuration v1ReadinessPost method.
     *
     * @param readinessCheckApplication application used to perform application readiness check.
     * @return readiness check result.
     */
    @Override
    public ReadinessCheckResult v1ReadinessPost(ReadinessCheckApplication readinessCheckApplication) {
        return null;
    }
}
