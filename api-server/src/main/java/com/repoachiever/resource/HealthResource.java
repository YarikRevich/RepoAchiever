package com.repoachiever.resource;

import com.repoachiever.api.HealthResourceApi;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.ReadinessCheckResult;
import com.repoachiever.service.client.smallrye.ISmallRyeHealthCheckClientService;
import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import java.net.URI;

/**
 * Contains implementation of HealthResource.
 */
@ApplicationScoped
public class HealthResource implements HealthResourceApi {
    @Inject
    ConfigService configService;

    ISmallRyeHealthCheckClientService smallRyeHealthCheckClientService;

    @PostConstruct
    private void configure() {
        smallRyeHealthCheckClientService = RestClientBuilder.newBuilder()
                .baseUri(
                        URI.create(
                                String.format(
                                        "http://localhost:%d", configService.getConfig().getConnection().getPort())))
                .build(ISmallRyeHealthCheckClientService.class);
    }

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
     * @param body application used to perform application readiness check.
     * @return readiness check result.
     */
    @Override
    public ReadinessCheckResult v1ReadinessPost(Object body) {
        throw new InternalServerErrorException();
    }
}
