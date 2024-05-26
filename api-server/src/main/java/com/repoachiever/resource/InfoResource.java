package com.repoachiever.resource;

import com.repoachiever.api.InfoResourceApi;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.CredentialsAreNotValidException;
import com.repoachiever.exception.CredentialsFieldIsNotValidException;
import com.repoachiever.exception.ProviderIsNotAvailableException;
import com.repoachiever.model.TopologyInfoApplication;
import com.repoachiever.model.VersionExternalApiInfoResult;
import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.resource.common.ResourceConfigurationHelper;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.cluster.facade.ClusterFacade;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Objects;

/**
 * Contains implementation of InfoResource.
 */
@ApplicationScoped
public class InfoResource implements InfoResourceApi {
    @Inject
    PropertiesEntity properties;

    @Inject
    ClusterFacade clusterFacade;

    @Inject
    VendorFacade vendorFacade;

    @Inject
    ResourceConfigurationHelper resourceConfigurationHelper;

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
     * Implementation for declared in OpenAPI configuration v1InfoTopologyPost method.
     *
     * @return topology information result.
     */
    @SneakyThrows
    @Override
    public List<TopologyInfoUnit> v1InfoTopologyPost(TopologyInfoApplication topologyInfoApplication) {
        if (Objects.isNull(topologyInfoApplication)) {
            throw new BadRequestException();
        }

        if (!resourceConfigurationHelper.isExternalCredentialsFieldValid(
                topologyInfoApplication.getProvider(),
                topologyInfoApplication.getCredentials().getExternal())) {
            throw new CredentialsFieldIsNotValidException();
        }

        if (!vendorFacade.isVendorAvailable(topologyInfoApplication.getProvider())) {
            throw new ProviderIsNotAvailableException();
        }

        if (!vendorFacade.isExternalCredentialsValid(
                topologyInfoApplication.getProvider(), topologyInfoApplication.getCredentials().getExternal())) {
            throw new CredentialsAreNotValidException();
        }

        return clusterFacade.retrieveTopology(topologyInfoApplication);
    }
}
