package com.repoachiever.service.cluster.facade;

import com.repoachiever.converter.ClusterContextToJsonConverter;
import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.entity.common.ClusterContextEntity;
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

import java.util.ArrayList;
import java.util.List;

/** Provides high-level access to RepoAchiever Cluster related operations. */
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
        List<String> removable = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException ignored) {
            }

            removable.add(clusterAllocation.getName());
        }

        StateService.removeClusterAllocationByNames(removable);

        List<List<String>> segregation = clusterService.performContentLocationsSegregation(
                contentApplication.getLocations(),
                configService.getConfig().getResource().getCluster().getMaxWorkers());

        for (List<String> locations : segregation) {
            String name = ClusterConfigurationHelper.getName(properties.getCommunicationClusterBase());

            ClusterContextEntity contextRaw =
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

            String context = ClusterContextToJsonConverter.convert(contextRaw);

            Integer pid;

            try {
                pid = clusterService.deploy(context);
            } catch (ClusterDeploymentFailureException e) {
                throw new ClusterApplicationFailureException(e.getMessage());
            }

            if (!ClusterConfigurationHelper.waitForStart(() -> {
                        try {
                            if (clusterClientResource.retrieveHealthCheck(name)) {
                                return true;
                            }
                        } catch (ClusterOperationFailureException e) {
                            return false;
                        }

                        return false;
                    },
                    properties.getCommunicationClusterStartupAwaitFrequency(),
                    properties.getCommunicationClusterStartupTimeout())) {
                throw new ClusterApplicationFailureException(new ClusterApplicationTimeoutException().getMessage());
            }

            StateService.addClusterAllocation(
                    ClusterAllocationDto.of(name, pid, context));
        }
    }

    // TODO: halt cluster during instance update operations, to exclude concurrency related issues.



    /**
     * Reapplies all unhealthy RepoAchiever Cluster allocations, which healthcheck operation failed for, recreating them.
     *
     * @throws ClusterUnhealthyReapplicationFailureException if RepoAchiever Cluster unhealthy allocation reapplication fails.
     */
    public void reapplyUnhealthy() throws ClusterUnhealthyReapplicationFailureException {
        List<ClusterAllocationDto> updates = new ArrayList<>();
        List<String> removable = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException ignored) {
            }

            Integer pid;

            try {
                pid = clusterService.deploy(clusterAllocation.getContext());
            } catch (ClusterDeploymentFailureException e) {
                throw new ClusterApplicationFailureException(e.getMessage());
            }

            removable.add(clusterAllocation.getName());

            updates.add(ClusterAllocationDto.of(
                    clusterAllocation.getName(), pid, clusterAllocation.getContext()));
        }

        StateService.removeClusterAllocationByNames(removable);

        updates.forEach(StateService::addClusterAllocation);
    }
}

// TODO: if cluster stops existing --> recreate it
// TODO: if api server stops existing --> selfdestruct all clusters