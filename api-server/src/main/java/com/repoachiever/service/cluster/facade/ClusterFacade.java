package com.repoachiever.service.cluster.facade;

import com.repoachiever.converter.ClusterContextToJsonConverter;
import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.entity.common.ConfigEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.cluster.common.ClusterConfigurationHelper;
import com.repoachiever.service.cluster.resource.ClusterClientResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ClusterFacade {
    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    @Inject
    ClusterService clusterService;

    @Inject
    ClusterClientResource clusterClientResource;

    /**
     * Applies given content application, removing previous topology and deploying new one with up-to-date configuration.
     *
     * @param contentApplication given content application used for topology configuration.
     * @throws ClusterApplicationFailureException if RepoAchiever Cluster application failed.
     */
    public void apply(ContentApplication contentApplication) throws ClusterApplicationFailureException {
        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                if (clusterClientResource.retrieveHealthCheck(clusterAllocation.getName())) {
                    try {
                        clusterService.destroy(clusterAllocation.getPid());
                    } catch (ClusterDestructionFailureException e) {
                        throw new ClusterApplicationFailureException(e.getMessage());
                    }
                }
            } catch (ClusterOperationFailureException ignored) {
            }

            StateService.removeClusterAllocationByName(clusterAllocation.getName());
        }

        List<List<String>> segregation = clusterService.performContentLocationsSegregation(
                contentApplication.getLocations(),
                configService.getConfig().getResource().getCluster().getMaxWorkers());

        System.out.println(contentApplication.getLocations());
        System.out.println(configService.getConfig().getResource().getCluster().getMaxWorkers());

        for (List<String> locations : segregation) {
            String name = ClusterConfigurationHelper.getName(properties.getCommunicationClusterBase());

            ClusterContextEntity clusterContext =
                    ClusterContextEntity.of(
                            name,
                            locations,
                            ClusterContextEntity.Communication.of(
                                    configService.getConfig().getCommunication().getPort()),
                            ClusterContextEntity.Content.of(
                                    configService.getConfig().getContent().getFormat()),
                            ClusterContextEntity.Resource.of(
                                    ClusterContextEntity.Resource.Cluster.of(
                                            configService.getConfig().getResource().getCluster().getMaxWorkers()),
                                    ClusterContextEntity.Resource.Worker.of(
                                            configService.getConfig().getResource().getWorker().getFrequency())));
            try {
                clusterService.deploy(
                        ClusterContextToJsonConverter.convert(clusterContext));
            } catch (ClusterDeploymentFailureException e) {
                throw new ClusterApplicationFailureException(e.getMessage());
            }

            if (!ClusterConfigurationHelper.waitForStart(() -> {
                        try {
                            if (clusterClientResource.retrieveHealthCheck(name)) {
                                return true;
                            }
                        } catch (ClusterOperationFailureException e) {
                            System.out.println(e.getMessage());
                            return false;
                        }

                        return false;
                    },
                    properties.getCommunicationClusterStartupAwaitFrequency(),
                    properties.getCommunicationClusterStartupTimeout())) {
                throw new ClusterApplicationFailureException(new ClusterApplicationTimeoutException().getMessage());
            }
        }
    }
}
