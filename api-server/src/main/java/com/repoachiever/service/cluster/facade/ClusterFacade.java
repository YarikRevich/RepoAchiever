package com.repoachiever.service.cluster.facade;

import com.repoachiever.converter.*;
import com.repoachiever.dto.ClusterAllocationDto;
import com.repoachiever.dto.RepositoryContentLocationUnitDto;
import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.*;
import com.repoachiever.repository.facade.RepositoryFacade;
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
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Provides high-level access to RepoAchiever Cluster related operations.
 */
@ApplicationScoped
public class ClusterFacade {
    private static final Logger logger = LogManager.getLogger(ClusterFacade.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    RepositoryFacade repositoryFacade;

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
     * Retrieves RepoAchiever Cluster content, using given content retrieval application.
     *
     * @param contentRetrievalApplication given content retrieval application.
     * @return retrieved content.
     * @throws ClusterContentRetrievalFailureException if RepoAchiever Cluster content retrieval failed.
     */
    public ContentRetrievalResult retrieveContent(ContentRetrievalApplication contentRetrievalApplication) throws
            ClusterContentRetrievalFailureException {
        StateService.getTopologyStateGuard().lock();

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(
                        contentRetrievalApplication.getProvider(), contentRetrievalApplication.getCredentials());

        List<String> locationUnits;

        try {
            locationUnits = workspaceFacade.getContentUnits(workspaceUnitKey);
        } catch (ContentUnitRetrievalFailureException e) {
            throw new ClusterContentRetrievalFailureException(e.getMessage());
        }

        List<RepositoryContentLocationUnitDto> repositoryContentLocations;

        try {
            repositoryContentLocations =
                    repositoryFacade.retrieveLocations(contentRetrievalApplication);
        } catch (ContentLocationsRetrievalFailureException e) {
            StateService.getTopologyStateGuard().unlock();

            throw new ClusterContentRetrievalFailureException(e.getMessage());
        }

        ContentRetrievalResult result = new ContentRetrievalResult();

        for (String locationUnit : locationUnits) {
            List<String> rawContentUnits;

            try {
                rawContentUnits = workspaceFacade.getRawContentUnits(workspaceUnitKey, locationUnit);
            } catch (RawContentUnitRetrievalFailureException e) {
                StateService.getTopologyStateGuard().unlock();

                throw new ClusterContentRetrievalFailureException(e.getMessage());
            }


            List<String> additionalContentUnits;

            try {
                additionalContentUnits = workspaceFacade.getAdditionalContentUnits(
                        workspaceUnitKey, locationUnit);
            } catch (AdditionalContentUnitRetrievalFailureException e) {
                StateService.getTopologyStateGuard().unlock();

                throw new ClusterContentRetrievalFailureException(e.getMessage());
            }

            Boolean active =
                    repositoryContentLocations
                            .stream()
                            .anyMatch(element -> Objects.equals(element.getLocation(), locationUnit));

            result.addLocationsItem(
                    ContentRetrievalUnit.of(
                            locationUnit,
                            active,
                            ContentRetrievalUnitRaw.of(rawContentUnits),
                            ContentRetrievalUnitAdditional.of(additionalContentUnits)));
        }

        for (RepositoryContentLocationUnitDto repositoryContentLocation : repositoryContentLocations) {
            if (!result
                    .getLocations()
                    .stream()
                    .anyMatch(element ->
                            Objects.equals(
                                    element.getName(), repositoryContentLocation.getLocation()))) {
                result.addLocationsItem(
                        ContentRetrievalUnit.of(
                                repositoryContentLocation.getLocation(),
                                true,
                                ContentRetrievalUnitRaw.of(new ArrayList<>()),
                                ContentRetrievalUnitAdditional.of(new ArrayList<>())));

            }
        }

        StateService.getTopologyStateGuard().unlock();

        return result;
    }

    /**
     * Applies given content application, removing previous topology and deploying new one with up-to-date configuration.
     *
     * @param contentApplication given content application used for topology configuration.
     * @throws ClusterApplicationFailureException if RepoAchiever Cluster application failed.
     */
    public void apply(ContentApplication contentApplication) throws ClusterApplicationFailureException {
        StateService.getTopologyStateGuard().lock();

        logger.info("Applying RepoAchiever Cluster content application");

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(
                        contentApplication.getProvider(), contentApplication.getCredentials());

        List<ClusterAllocationDto> suspends = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.
                getClusterAllocationsByWorkspaceUnitKey(workspaceUnitKey)) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster allocation to suspend state: '%s'",
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

        List<List<LocationsUnit>> segregation = clusterService.performContentLocationsSegregation(
                contentApplication.getContent().getLocations(),
                configService.getConfig().getResource().getCluster().getMaxWorkers());

        List<ClusterAllocationDto> candidates = new ArrayList<>();

        for (List<LocationsUnit> locations : segregation) {
            String name = ClusterConfigurationHelper.getName(properties.getCommunicationClusterBase());

            String context = ClusterContextToJsonConverter.convert(
                    ClusterContextEntity.of(
                            ClusterContextEntity.Metadata.of(name, workspaceUnitKey),
                            ClusterContextEntity.Content.of(
                                    ContentLocationsToClusterContextLocationsConverter.convert(locations),
                                    ContentFormatToClusterContextFormatConverter.convert(
                                            configService.getConfig().getContent().getFormat())),
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
                            ClusterContextEntity.Resource.of(
                                    ClusterContextEntity.Resource.Worker.of(
                                            configService.getConfig().getResource().getWorker().getFrequency()))));

            logger.info(
                    String.format("Deploying RepoAchiever Cluster new allocation: '%s'", name));

            Integer pid;

            try {
                pid = clusterService.deploy(name, context);
            } catch (ClusterDeploymentFailureException e1) {
                for (ClusterAllocationDto candidate : candidates) {
                    logger.info(
                            String.format("Removing RepoAchiever Cluster candidate allocation: '%s'", candidate.getName()));

                    try {
                        clusterService.destroy(candidate.getPid());
                    } catch (ClusterDestructionFailureException e2) {
                        StateService.getTopologyStateGuard().unlock();

                        throw new ClusterApplicationFailureException(e1.getMessage(), e2.getMessage());
                    }
                }

                for (ClusterAllocationDto suspended : suspends) {
                    logger.info(
                            String.format("Setting RepoAchiever Cluster suspended allocation to serve state: '%s'", suspended.getName()));

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

            for (LocationsUnit location : locations) {
                try {
                    workspaceFacade.createContentDirectory(workspaceUnitKey, location.getName());
                } catch (UnitDirectoryCreationFailureException e) {
                    logger.fatal(new ClusterApplicationFailureException(e.getMessage()).getMessage());

                    return;
                }
            }

            candidates.add(ClusterAllocationDto.of(name, workspaceUnitKey, locations, pid, context));
        }

        for (ClusterAllocationDto candidate : candidates) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster candidate allocation to serve state: '%s'",
                            candidate.getName()));

            try {
                clusterCommunicationResource.performServe(candidate.getName());
            } catch (ClusterOperationFailureException e1) {
                for (ClusterAllocationDto suspended : suspends) {
                    logger.info(
                            String.format(
                                    "Setting RepoAchiever Cluster suspended allocation to serve state: '%s'",
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
                    String.format("Removing RepoAchiever Cluster suspended allocation: '%s'", suspended.getName()));

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

        logger.info(String.format("Destroying RepoAchiever Cluster topology for: '%s'", workspaceUnitKey));

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
                    String.format("Removing RepoAchiever Cluster allocation: '%s'", clusterAllocation.getName()));

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

        logger.info("Destroying all RepoAchiever Cluster topology");

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocations()) {
            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());
            } catch (ClusterOperationFailureException ignored) {
                logger.info(
                        String.format("RepoAchiever Cluster allocation is not responding on suspend request: '%s'",
                                clusterAllocation.getName()));
            }

            logger.info(
                    String.format("Removing RepoAchiever Cluster allocation: '%s'", clusterAllocation.getName()));

            try {
                clusterService.destroy(clusterAllocation.getPid());
            } catch (ClusterDestructionFailureException e) {
                throw new ClusterFullDestructionFailureException(e.getMessage());
            }
        }
    }

    /**
     * Removes content from the workspace with the help of the given application.
     *
     * @param contentCleanup given content cleanup application used for content removal.
     * @throws ClusterCleanupFailureException if RepoAchiever Cluster cleanup failed.
     */
    public void removeContent(ContentCleanup contentCleanup) throws ClusterCleanupFailureException {
        StateService.getTopologyStateGuard().lock();

        logger.info(String.format("Removing content of '%s' location", contentCleanup.getLocation()));

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(contentCleanup.getProvider(), contentCleanup.getCredentials());

        ClusterAllocationDto clusterAllocation = StateService
                .getClusterAllocationByWorkspaceUnitKeyAndName(workspaceUnitKey, contentCleanup.getLocation());

        if (Objects.nonNull(clusterAllocation)) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster allocation to suspend state: '%s'",
                            clusterAllocation.getName()));

            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());

            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterCleanupFailureException(e.getMessage()).getMessage());

                return;
            }

            telemetryService.decreaseServingClustersAmount();

            telemetryService.increaseSuspendedClustersAmount();
        }

        try {
            workspaceFacade.removeContent(workspaceUnitKey, contentCleanup.getLocation());
        } catch (ContentRemovalFailureException e) {
            StateService.getTopologyStateGuard().unlock();

            throw new ClusterCleanupFailureException(e.getMessage());
        }

        if (Objects.nonNull(clusterAllocation)) {
            logger.info(
                    String.format(
                            "Resetting RepoAchiever Cluster content retrieval: '%s'", clusterAllocation.getName()));

            try {
                clusterCommunicationResource.performRetrievalReset(clusterAllocation.getName());
            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterCleanupFailureException(e.getMessage()).getMessage());

                return;
            }

            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster suspended allocation to serve state: '%s'",
                            clusterAllocation.getName()));

            try {
                clusterCommunicationResource.performServe(clusterAllocation.getName());
            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterCleanupFailureException(e.getMessage()).getMessage());

                return;
            }

            telemetryService.decreaseSuspendedClustersAmount();

            telemetryService.increaseServingClustersAmount();
        }

        StateService.getTopologyStateGuard().unlock();
    }

    /**
     * Removes all the content from the workspace with the help of the given application.
     *
     * @param contentCleanupAll given content full cleanup application used for content removal.
     * @throws ClusterFullCleanupFailureException if RepoAchiever Cluster cleanup failed.
     */
    public void removeAll(ContentCleanupAll contentCleanupAll) throws ClusterFullCleanupFailureException {
        StateService.getTopologyStateGuard().lock();

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(contentCleanupAll.getProvider(), contentCleanupAll.getCredentials());

        List<ClusterAllocationDto> suspends = new ArrayList<>();

        for (ClusterAllocationDto clusterAllocation : StateService.
                getClusterAllocationsByWorkspaceUnitKey(workspaceUnitKey)) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster allocation to suspend state: '%s'",
                            clusterAllocation.getName()));

            try {
                clusterCommunicationResource.performSuspend(clusterAllocation.getName());

            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterFullCleanupFailureException(e.getMessage()).getMessage());

                return;
            }

            suspends.add(clusterAllocation);

            telemetryService.decreaseServingClustersAmount();

            telemetryService.increaseSuspendedClustersAmount();
        }

        try {
            workspaceFacade.removeAll(workspaceUnitKey);
        } catch (ContentRemovalFailureException e) {
            StateService.getTopologyStateGuard().unlock();

            throw new ClusterFullCleanupFailureException(e.getMessage());
        }

        for (ClusterAllocationDto suspended : suspends) {
            logger.info(
                    String.format(
                            "Resetting RepoAchiever Cluster content retrieval: '%s'", suspended.getName()));

            try {
                clusterCommunicationResource.performRetrievalReset(suspended.getName());
            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterCleanupFailureException(e.getMessage()).getMessage());

                return;
            }

            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster suspended allocation to serve state: '%s'",
                            suspended.getName()));

            try {
                clusterCommunicationResource.performServe(suspended.getName());
            } catch (ClusterOperationFailureException e) {
                logger.fatal(new ClusterFullCleanupFailureException(e.getMessage()).getMessage());

                return;
            }

            telemetryService.decreaseSuspendedClustersAmount();

            telemetryService.increaseServingClustersAmount();
        }

        StateService.getTopologyStateGuard().unlock();
    }

    /**
     * Retrieves content reference with the help of the given content download application.
     *
     * @param contentDownload given content download application.
     * @return retrieved content download reference.
     * @throws ClusterContentReferenceRetrievalFailureException if RepoAchiever Cluster content reference retrieval
     *                                                          operation failed.
     */
    public byte[] retrieveContentReference(ContentDownload contentDownload) throws
            ClusterContentReferenceRetrievalFailureException {
        StateService.getTopologyStateGuard().lock();

        logger.info(String.format("Retrieving content for '%s' location", contentDownload.getLocation()));

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(contentDownload.getProvider(), contentDownload.getCredentials());

        Boolean result;

        try {
            result = workspaceFacade.isAnyContentAvailable(workspaceUnitKey, contentDownload.getLocation());
        } catch (ContentAvailabilityRetrievalFailureException e) {
            StateService.getTopologyStateGuard().unlock();

            throw new ClusterContentReferenceRetrievalFailureException(
                    new ClusterContentAvailabilityRetrievalFailureException(e.getMessage()).getMessage());
        }

        if (!result) {
            StateService.getTopologyStateGuard().unlock();

            throw new ClusterContentReferenceRetrievalFailureException(
                    new ClusterContentAvailabilityRetrievalFailureException().getMessage());
        }

        byte[] contentReference;

        try {
            contentReference = workspaceFacade.createContentReference(workspaceUnitKey, contentDownload.getLocation());
        } catch (ContentReferenceCreationFailureException e) {
            StateService.getTopologyStateGuard().unlock();

            throw new ClusterContentReferenceRetrievalFailureException(e.getMessage());
        }

        StateService.getTopologyStateGuard().unlock();

        return contentReference;
    }

    /**
     * Retrieves RepoAchiever Cluster allocation topology with the given application.
     *
     * @param topologyInfoApplication given topology retrieval application.
     * @return retrieved topology information.
     */
    public List<TopologyInfoUnit> retrieveTopology(TopologyInfoApplication topologyInfoApplication) {
        List<TopologyInfoUnit> result = new ArrayList<>();

        String workspaceUnitKey =
                workspaceFacade.createUnitKey(
                        topologyInfoApplication.getProvider(), topologyInfoApplication.getCredentials());

        for (ClusterAllocationDto clusterAllocation : StateService.getClusterAllocationsByWorkspaceUnitKey(
                workspaceUnitKey)) {
            result.add(TopologyInfoUnit.of(clusterAllocation.getName(), clusterAllocation.getLocations()));
        }

        return result;
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
                Boolean healthy =
                        clusterCommunicationResource.retrieveHealthCheck(clusterAllocation.getName());

                if (!healthy) {
                    logger.info(
                            String.format(
                                    "Setting RepoAchiever Cluster allocation to suspend state: '%s'",
                                    clusterAllocation.getName()));

                    try {
                        clusterCommunicationResource.performSuspend(clusterAllocation.getName());
                    } catch (ClusterOperationFailureException ignored) {
                        logger.info(
                                String.format("RepoAchiever Cluster allocation is not responding on suspend request: '%s'",
                                        clusterAllocation.getName()));
                    }

                    telemetryService.increaseSuspendedClustersAmount();

                    telemetryService.decreaseServingClustersAmount();

                    logger.info(
                            String.format("Removing RepoAchiever Cluster allocation: '%s'", clusterAllocation.getName()));

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
                        String.format("Removing RepoAchiever Cluster allocation: '%s'", clusterAllocation.getName()));

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
                    String.format("Redeploying RepoAchiever Cluster allocation: '%s'", removable.getName()));

            Integer pid;

            try {
                pid = clusterService.deploy(removable.getName(), removable.getContext());
            } catch (ClusterDeploymentFailureException e) {
                StateService.getTopologyStateGuard().unlock();

                throw new ClusterUnhealthyReapplicationFailureException(e.getMessage());
            }

            candidates.add(ClusterAllocationDto.of(
                    removable.getName(),
                    removable.getWorkspaceUnitKey(),
                    removable.getLocations(),
                    pid,
                    removable.getContext()));
        }

        for (ClusterAllocationDto candidate : candidates) {
            logger.info(
                    String.format(
                            "Setting RepoAchiever Cluster candidate allocation to serve state: '%s'",
                            candidate.getName()));

            try {
                clusterCommunicationResource.performServe(candidate.getName());
            } catch (ClusterOperationFailureException e1) {
                StateService.getTopologyStateGuard().unlock();

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