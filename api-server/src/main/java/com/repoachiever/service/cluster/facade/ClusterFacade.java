package com.repoachiever.service.cluster.facade;

import com.repoachiever.converter.ClusterContextToJsonConverter;
import com.repoachiever.converter.ContentCredentialsToClusterContextCredentialsConverter;
import com.repoachiever.converter.ContentProviderToClusterContextProviderConverter;
import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.cluster.common.ClusterConfigurationHelper;
import com.repoachiever.service.cluster.resource.ClusterCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.communication.cluster.topology.ClusterTopologyCommunicationConfigService;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.workspace.facade.WorkspaceFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides high-level access to RepoAchiever Cluster related operations.
 */
@ApplicationScoped
public class ClusterFacade {
    private static final Logger logger = LogManager.getLogger(ClusterFacade.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    WorkspaceFacade workspaceFacade;

    @Inject
    ConfigService configService;

    @Inject
    ClusterService clusterService;

    @Inject
    ClusterCommunicationResource clusterCommunicationResource;

    /**
     * Applies given content application, removing previous topology and deploying new one with up-to-date configuration.
     *
     * @param contentApplication given content application used for topology configuration.
     * @throws ClusterApplicationFailureException if RepoAchiever Cluster application failed.
     */
    public void apply(ContentApplication contentApplication) throws ClusterApplicationFailureException {
        StateService.getTopologyStateGuard().lock();

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(
                        contentApplication.getProvider(), contentApplication.getCredentials());

        List<ClusterAllocationDto> suspends = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.
                getClusterAllocationsByWorkspaceUnitKey(workspaceUnitKey)) {
            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());

            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterApplicationFailureException(e.getMessage()).getMessage());
                return;
            }

            suspends.add(clusterAllocation);
        }

        List<List<String>> segregation = clusterService.performContentLocationsSegregation(
                contentApplication.getLocations(),
                configService.getConfig().getResource().getCluster().getMaxWorkers());

        List<ClusterAllocationDto> candidates = new ArrayList<>();

        for (List<String> locations : segregation) {
            String name = ClusterConfigurationHelper.getName(properties.getCommunicationClusterBase());

            String context = ClusterContextToJsonConverter.convert(
                    ClusterContextEntity.of(
                            ClusterContextEntity.Metadata.of(name, workspaceUnitKey),
                            ClusterContextEntity.Filter.of(locations),
                            ClusterContextEntity.Service.of(
                                    ContentProviderToClusterContextProviderConverter.convert(
                                            contentApplication.getProvider()),
                                    ContentCredentialsToClusterContextCredentialsConverter.convert(
                                            contentApplication.getProvider(),
                                            contentApplication.getCredentials().getExternal())),
                            ClusterContextEntity.Communication.of(
                                    configService.getConfig().getCommunication().getPort()),
                            ClusterContextEntity.Content.of(
                                    configService.getConfig().getContent().getFormat()),
                            ClusterContextEntity.Resource.of(
                                    ClusterContextEntity.Resource.Cluster.of(
                                            configService.getConfig().getResource().getCluster().getMaxWorkers()),
                                    ClusterContextEntity.Resource.Worker.of(
                                            configService.getConfig().getResource().getWorker().getFrequency()))));

            Integer pid;

            try {
                pid = clusterService.deploy(name, context);
            } catch (ClusterDeploymentFailureException e1) {
                for (ClusterAllocationDto candidate : candidates) {
                    try {
                        clusterService.destroy(candidate.getPid());
                    } catch (ClusterDestructionFailureException e2) {
                        throw new ClusterApplicationFailureException(e1.getMessage(), e2.getMessage());
                    }
                }

                for (ClusterAllocationDto suspended : suspends) {
                    try {
                        clusterCommunicationResource.performServe(suspended.getName());
                    } catch (ClusterOperationFailureException e2) {
                        logger.fatal(new ClusterApplicationFailureException(e1.getMessage(), e2.getMessage()).getMessage());
                        return;
                    }
                }

                throw new ClusterApplicationFailureException(e1.getMessage());
            }

            candidates.add(ClusterAllocationDto.of(name, pid, context, workspaceUnitKey));
        }

        for (ClusterAllocationDto suspended : suspends) {
            try {
                clusterService.destroy(suspended.getPid());
            } catch (ClusterDestructionFailureException e) {
                throw new ClusterApplicationFailureException(e.getMessage());
            }
        }

        candidates.forEach(StateService::addClusterAllocation);

        StateService.removeClusterAllocationByNames(suspends.stream().map(ClusterAllocationDto::getName).toList());

        StateService.getTopologyStateGuard().unlock();
    }

    // TODO: halt cluster during instance update operations, to exclude concurrency related issues.


    /**
     * Reapplies all unhealthy RepoAchiever Cluster allocations, which healthcheck operation failed for, recreating them.
     *
     * @throws ClusterUnhealthyReapplicationFailureException if RepoAchiever Cluster unhealthy allocation reapplication fails.
     */
    public void reapplyUnhealthy() throws ClusterUnhealthyReapplicationFailureException {
        StateService.getTopologyStateGuard().lock();


        List<ClusterAllocationDto> updates = new ArrayList<>();
        List<String> removable = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException ignored) {
            }

            Integer pid;

            try {
                pid = clusterService.deploy(clusterAllocation.getName(), clusterAllocation.getContext());
            } catch (ClusterDeploymentFailureException e) {
                throw new ClusterUnhealthyReapplicationFailureException(e.getMessage());
            }

            removable.add(clusterAllocation.getName());
//
//            updates.add(ClusterAllocationDto.of(
//                    clusterAllocation.getName(), pid, clusterAllocation.getContext()));
        }

        StateService.removeClusterAllocationByNames(removable);

        updates.forEach(StateService::addClusterAllocation);


        StateService.getTopologyStateGuard().unlock();
    }
}

// TODO: if cluster stops existing --> recreate it
// TODO: if api server stops existing --> selfdestruct all clusters