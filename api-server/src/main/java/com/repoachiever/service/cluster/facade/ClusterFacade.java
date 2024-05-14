package com.repoachiever.service.cluster.facade;

import com.repoachiever.converter.ClusterContextToJsonConverter;
import com.repoachiever.converter.ContentCredentialsToClusterContextCredentialsConverter;
import com.repoachiever.converter.ContentExporterToClusterContextExporterConverter;
import com.repoachiever.converter.ContentProviderToClusterContextProviderConverter;
import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.model.ContentWithdrawal;
import com.repoachiever.service.cluster.ClusterService;
import com.repoachiever.service.cluster.common.ClusterConfigurationHelper;
import com.repoachiever.service.cluster.resource.ClusterCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.telemetry.TelemetryService;
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
    ConfigService configService;

    @Inject
    WorkspaceFacade workspaceFacade;

    @Inject
    ClusterService clusterService;

    @Inject
    ClusterCommunicationResource clusterCommunicationResource;

    @Inject
    TelemetryService telemetryService;

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
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster allocation to suspend state: %s",
                            clusterAllocation.getName()));

            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());

            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterApplicationFailureException(e.getMessage()).getMessage());
                return;
            }

            suspends.add(clusterAllocation);

            telemetryService.decreaseServingClustersAmount();

            telemetryService.increaseSuspendedClustersAmount();
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
                                    ContentExporterToClusterContextExporterConverter.convert(
                                            contentApplication.getProvider(), contentApplication.getExporter()),
                                    ContentCredentialsToClusterContextCredentialsConverter.convert(
                                            contentApplication.getProvider(),
                                            contentApplication.getCredentials().getExternal())),
                            ClusterContextEntity.Communication.of(
                                    properties.getCommunicationApiServerName(),
                                    configService.getConfig().getCommunication().getPort()),
                            ClusterContextEntity.Content.of(
                                    configService.getConfig().getContent().getFormat()),
                            ClusterContextEntity.Resource.of(
                                    ClusterContextEntity.Resource.Cluster.of(
                                            configService.getConfig().getResource().getCluster().getMaxWorkers()),
                                    ClusterContextEntity.Resource.Worker.of(
                                            configService.getConfig().getResource().getWorker().getFrequency()))));

            logger.info(
                    String.format("Deploying RepoAchiever Cluster new allocation: %s", name));

            Integer pid;

            try {
                pid = clusterService.deploy(name, context);
            } catch (ClusterDeploymentFailureException e1) {
                for (ClusterAllocationDto candidate : candidates) {
                    logger.info(
                            String.format("Removing RepoAchiever Cluster candidate allocation: %s", candidate.getName()));

                    try {
                        clusterService.destroy(candidate.getPid());
                    } catch (ClusterDestructionFailureException e2) {
                        StateService.getTopologyStateGuard().unlock();

                        throw new ClusterApplicationFailureException(e1.getMessage(), e2.getMessage());
                    }
                }

                for (ClusterAllocationDto suspended : suspends) {
                    logger.info(
                            String.format("Setting RepoAchiever Cluster suspended allocation to serve state: %s", suspended.getName()));

                    try {
                        clusterCommunicationResource.performServe(suspended.getName());
                    } catch (ClusterOperationFailureException e2) {
                        logger.fatal(new ClusterApplicationFailureException(e1.getMessage(), e2.getMessage()).getMessage());
                        return;
                    }

                    telemetryService.decreaseSuspendedClustersAmount();

                    telemetryService.increaseServingClustersAmount();
                }

                StateService.getTopologyStateGuard().unlock();

                throw new ClusterApplicationFailureException(e1.getMessage());
            }

            candidates.add(ClusterAllocationDto.of(name, pid, context, workspaceUnitKey));
        }

        for (ClusterAllocationDto candidate : candidates) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster candidate allocation to serve state: %s",
                            candidate.getName()));

            try {
                clusterCommunicationResource.performServe(candidate.getName());
            } catch (ClusterOperationFailureException e1) {
                for (ClusterAllocationDto suspended : suspends) {
                    logger.info(
                            String.format(
                                    "Setting RepoAchiever Cluster suspended allocation to serve state: %s",
                                    suspended.getName()));

                    try {
                        clusterCommunicationResource.performServe(suspended.getName());
                    } catch (ClusterOperationFailureException e2) {
                        logger.fatal(new ClusterApplicationFailureException(
                                e1.getMessage(), e2.getMessage()).getMessage());
                        return;
                    }

                    telemetryService.decreaseSuspendedClustersAmount();

                    telemetryService.increaseServingClustersAmount();
                }

                StateService.getTopologyStateGuard().unlock();

                throw new ClusterApplicationFailureException(e1.getMessage());
            }

            telemetryService.increaseServingClustersAmount();
        }

        for (ClusterAllocationDto suspended : suspends) {
            logger.info(
                    String.format("Removing RepoAchiever Cluster suspended allocation: %s", suspended.getName()));

            try {
                clusterService.destroy(suspended.getPid());
            } catch (ClusterDestructionFailureException e) {
                StateService.getTopologyStateGuard().unlock();

                throw new ClusterApplicationFailureException(e.getMessage());
            }

            telemetryService.decreaseSuspendedClustersAmount();
        }

        StateService.addClusterAllocations(candidates);

        StateService.removeClusterAllocationByNames(
                suspends.stream().map(ClusterAllocationDto::getName).toList());

        StateService.getTopologyStateGuard().unlock();
    }

    /**
     * Applies given content withdrawal, removing existing content configuration with the given properties.
     *
     * @param contentWithdrawal given content application used for topology configuration.
     * @throws ClusterWithdrawalFailureException if RepoAchiever Cluster withdrawal failed.
     */
    public void destroy(ContentWithdrawal contentWithdrawal) throws ClusterWithdrawalFailureException {
        StateService.getTopologyStateGuard().lock();

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(
                        contentWithdrawal.getProvider(), contentWithdrawal.getCredentials());

        List<ClusterAllocationDto> clusterAllocations =
                StateService.getClusterAllocationsByWorkspaceUnitKey(workspaceUnitKey);

        for (ClusterAllocationDto clusterAllocation : clusterAllocations) {
            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());
            } catch (ClusterOperationFailureException e) {
                StateService.getTopologyStateGuard().unlock();

                throw new ClusterWithdrawalFailureException(e.getMessage());
            }

            telemetryService.increaseSuspendedClustersAmount();

            telemetryService.decreaseServingClustersAmount();

            logger.info(
                    String.format("Removing RepoAchiever Cluster allocation: %s", clusterAllocation.getName()));

            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException e) {
                StateService.getTopologyStateGuard().unlock();

                throw new ClusterWithdrawalFailureException(e.getMessage());
            }

            telemetryService.decreaseSuspendedClustersAmount();
        }

        StateService.removeClusterAllocationByNames(
                clusterAllocations.stream().
                        map(ClusterAllocationDto::getName).
                        toList());

        StateService.getTopologyStateGuard().unlock();
    }

    /**
     * Destroys all the created RepoAchiever Cluster allocations.
     *
     * @throws ClusterFullDestructionFailureException if RepoAchiever Cluster full destruction failed.
     */
    public void destroyAll() throws ClusterFullDestructionFailureException {
        StateService.getTopologyStateGuard().lock();

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());
            } catch (ClusterOperationFailureException ignored) {
                logger.info(
                        String.format("RepoAchiever Cluster allocation is not responding on suspend request: %s",
                                clusterAllocation.getName()));
            }

            telemetryService.increaseSuspendedClustersAmount();

            telemetryService.decreaseServingClustersAmount();

            logger.info(
                    String.format("Removing RepoAchiever Cluster allocation: %s", clusterAllocation.getName()));

            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException e) {
                throw new ClusterFullDestructionFailureException(e.getMessage());
            }

            telemetryService.decreaseSuspendedClustersAmount();
        }
    }

    /**
     * Reapplies all unhealthy RepoAchiever Cluster allocations, which healthcheck operation failed for, recreating them.
     *
     * @throws ClusterUnhealthyReapplicationFailureException if RepoAchiever Cluster unhealthy allocation reapplication fails.
     */
    public void reApplyUnhealthy() throws ClusterUnhealthyReapplicationFailureException {
        StateService.getTopologyStateGuard().lock();

        List<ClusterAllocationDto> removables = new ArrayList<>();

        List<ClusterAllocationDto> candidates = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                if (!clusterCommunicationResource.retrieveHealthCheck(clusterAllocation.getName())) {
                    logger.info(
                            String.format(
                                    "Setting RepoAchiever Cluster allocation to suspend state: %s",
                                    clusterAllocation.getName()));

                    try {
                        clusterCommunicationResource.performSuspend(clusterAllocation.getName());
                    } catch (ClusterOperationFailureException ignored) {
                        logger.info(
                                String.format("RepoAchiever Cluster allocation is not responding on suspend request: %s",
                                        clusterAllocation.getName()));
                    }

                    telemetryService.increaseSuspendedClustersAmount();

                    telemetryService.decreaseServingClustersAmount();

                    logger.info(
                            String.format("Removing RepoAchiever Cluster allocation: %s", clusterAllocation.getName()));

                    try {
                        clusterService.destroy(clusterAllocation.getPid());
                    } catch (ClusterDestructionFailureException ignored) {
                    }

                    telemetryService.decreaseSuspendedClustersAmount();
                } else {
                    continue;
                }
            } catch (ClusterOperationFailureException e) {
                logger.info(
                        String.format("Removing RepoAchiever Cluster allocation: %s", clusterAllocation.getName()));

                try {
                    clusterService.destroy(clusterAllocation.getPid());
                } catch (ClusterDestructionFailureException ignored) {
                }

                telemetryService.decreaseServingClustersAmount();
            }

            removables.add(clusterAllocation);
        }

        if (removables.isEmpty()) {
            StateService.getTopologyStateGuard().unlock();

            return;
        }

        for (ClusterAllocationDto removable : removables) {
            logger.info(
                    String.format("Redeploying RepoAchiever Cluster allocation: %s", removable.getName()));

            Integer pid;

            try {
                pid = clusterService.deploy(removable.getName(), removable.getContext());
            } catch (ClusterDeploymentFailureException e) {
                throw new ClusterUnhealthyReapplicationFailureException(e.getMessage());
            }

            candidates.add(ClusterAllocationDto.of(
                    removable.getName(),
                    pid,
                    removable.getContext(),
                    removable.getWorkspaceUnitKey()));
        }

        for (ClusterAllocationDto candidate : candidates) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster candidate allocation to serve state: %s",
                            candidate.getName()));

            try {
                clusterCommunicationResource.performServe(candidate.getName());
            } catch (ClusterOperationFailureException e1) {
                throw new ClusterUnhealthyReapplicationFailureException(e1.getMessage());
            }

            telemetryService.increaseServingClustersAmount();
        }

        StateService.removeClusterAllocationByNames(
                removables.stream().map(ClusterAllocationDto::getName).toList());

        StateService.addClusterAllocations(candidates);

        StateService.getTopologyStateGuard().unlock();
    }
}