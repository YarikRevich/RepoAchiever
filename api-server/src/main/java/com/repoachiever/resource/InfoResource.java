package com.repoachiever.resource;

import com.amazonaws.services.kafka.model.ClusterInfo;
import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.ClusterInfoUnit;
import com.repoachiever.model.VersionExternalApiInfoResult;
import com.repoachiever.model.VersionInfoResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/**
 * Contains implementation of InfoResource.
 */
@ApplicationScoped
public class InfoResource implements InfoResourceApi {
    @Inject
    PropertiesEntity properties;

    /**
     * Implementation for declared in OpenAPI configuration v1InfoVersionGet method.
     *
     * @return Version information result.
     */
    @Override
    public VersionInfoResult v1InfoVersionGet() {
        return VersionInfoResult.of(
                VersionExternalApiInfoResult.of(
                        properties.getApplicationVersion(), properties.getGitCommitId()));
    }

    /**
     * Implementation for declared in OpenAPI configuration v1InfoClusterGet method.
     *
     * @return Cluster information result.
     */
    @Override
    public List<ClusterInfoUnit> v1InfoClusterGet() {
        // TODO: call cluster service to retrieve data from clusters.

        return null;
    }

    /**
     * Implementation for declared in OpenAPI configuration v1InfoTelemetryGet method.
     *
     * @return Telemetry information result.
     */
    @Override
    public String v1InfoTelemetryGet() {
        // TODO: call telemetry service to retrieve data.

        return null;
    }
}
