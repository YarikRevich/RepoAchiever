package com.repoachiever.resource;

import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.model.VersionExternalApiInfoResult;
import com.repoachiever.model.TopologyInfoUnit;
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
     * @return version information result.
     */
    @Override
    public VersionInfoResult v1InfoVersionGet() {
        return VersionInfoResult.of(
                VersionExternalApiInfoResult.of(
                        properties.getApplicationVersion(), properties.getGitCommitId()));
    }

    /**
     * Implementation for declared in OpenAPI configuration v1InfoTopologyGet method.
     *
     * @return topology information result.
     */
    @Override
    public List<TopologyInfoUnit> v1InfoTopologyGet() {
        // TODO: call cluster service to retrieve data from clusters.

        return null;
    }
}
