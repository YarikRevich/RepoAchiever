package com.repoachiever.service.cluster;

import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.service.cluster.common.ClusterConfigurationHelper;
import com.repoachiever.service.cluster.resource.ClusterCommunicationResource;
import com.repoachiever.service.command.cluster.deploy.ClusterDeployCommandService;
import com.repoachiever.service.command.cluster.destroy.ClusterDestroyCommandService;
import com.repoachiever.service.executor.CommandExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service used for cluster deployment management, including distribution process.
 */
@ApplicationScoped
public class ClusterService {
    @Inject
    PropertiesEntity properties;

    @Inject
    ClusterCommunicationResource clusterCommunicationResource;

    @Inject
    CommandExecutorService commandExecutorService;

    /**
     * Perform segregation of the given content locations according to the given segregation limitations.
     *
     * @param locations given content locations.
     * @param separator given content location segregation separator.
     * @return segregated content locations.
     */
    public List<List<String>> performContentLocationsSegregation(List<String> locations, Integer separator) {
        List<List<String>> result = new ArrayList<>();

        List<String> temp = new ArrayList<>();

        Integer counter = 0;

        for (Integer i = 0; i < locations.size(); i++) {
            temp.add(locations.get(0));

            if (counter > separator) {
                result.add(new ArrayList<>(temp));

                temp.clear();
            } else {
                counter++;
            }
        }

        if (!temp.isEmpty()) {
            result.add(new ArrayList<>(temp));
        }

        return result;
    }

    /**
     * Performs deployment of RepoAchiever Cluster allocation.
     *
     * @param name given RepoAchiever Cluster allocation name.
     * @param clusterContext given RepoAchiever Cluster context.
     * @return process identificator of the deployed RepoAchiever Cluster instance.
     * @throws ClusterDeploymentFailureException if deployment operation failed.
     */
    public Integer deploy(String name, String clusterContext) throws ClusterDeploymentFailureException {
        ClusterDeployCommandService clusterDeployCommandService =
                new ClusterDeployCommandService(
                        clusterContext,
                        properties.getBinDirectory(),
                        properties.getBinClusterLocation());

        CommandExecutorOutputDto clusterDeployCommandOutput;

        try {
            clusterDeployCommandOutput =
                    commandExecutorService.executeCommand(clusterDeployCommandService);
        } catch (CommandExecutorException e) {
            throw new ClusterDeploymentFailureException(e.getMessage());
        }

        String clusterDeployCommandErrorOutput = clusterDeployCommandOutput.getErrorOutput();

        if (Objects.nonNull(clusterDeployCommandErrorOutput) && !clusterDeployCommandErrorOutput.isEmpty()) {
            throw new ClusterDeploymentFailureException();
        }

        Integer result = Integer.parseInt(
                clusterDeployCommandOutput.
                        getNormalOutput().
                        replaceAll("\n", ""));

        if (!ClusterConfigurationHelper.waitForStart(() -> {
                    try {
                        if (clusterCommunicationResource.retrieveHealthCheck(name)) {
                            return true;
                        }
                    } catch (ClusterOperationFailureException e) {
                        return false;
                    }

                    return false;
                },
                properties.getCommunicationClusterStartupAwaitFrequency(),
                properties.getCommunicationClusterStartupTimeout())) {
            throw new ClusterDeploymentFailureException(new ClusterApplicationTimeoutException().getMessage());
        }

        return result;
    }

    /**
     * Performs destruction of RepoAchiever Cluster allocation.
     *
     * @param pid given RepoAchiever Cluster allocation process id.
     * @throws ClusterDestructionFailureException if destruction operation failed.
     */
    public void destroy(Integer pid) throws ClusterDestructionFailureException {
        ClusterDestroyCommandService clusterDestroyCommandService = new ClusterDestroyCommandService(pid);

        CommandExecutorOutputDto clusterDestroyCommandOutput;

        try {
            clusterDestroyCommandOutput =
                    commandExecutorService.executeCommand(clusterDestroyCommandService);
        } catch (CommandExecutorException e) {
            throw new ClusterDestructionFailureException(e.getMessage());
        }

        String clusterDestroyCommandErrorOutput = clusterDestroyCommandOutput.getErrorOutput();

        if (Objects.nonNull(clusterDestroyCommandErrorOutput) && !clusterDestroyCommandErrorOutput.isEmpty()) {
            throw new ClusterDestructionFailureException();
        }
    }

    /**
     * Performs recreation of RepoAchiever Cluster allocation.
     *
     * @param pid given process identificator of the allocation RepoAchiever Cluster to be removed.
     * @param name given RepoAchiever Cluster allocation name.
     * @param clusterContext given RepoAchiever Cluster context used for the new allocation.
     * @throws ClusterRecreationFailureException if recreation operation failed.
     */
    public Integer recreate(Integer pid, String name, String clusterContext) throws ClusterRecreationFailureException {
        try {
            destroy(pid);
        } catch (ClusterDestructionFailureException e) {
            throw new ClusterRecreationFailureException(e.getMessage());
        }

        try {
            return deploy(name, clusterContext);
        } catch (ClusterDeploymentFailureException e) {
            throw new ClusterRecreationFailureException(e.getMessage());
        }
    }
}


// TODO: make assignment of random identificators

// TODO: probably move this logic to ClusterService

// TODO: should regenerate topology after each location added