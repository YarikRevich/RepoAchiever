package com.repoachiever.service.cluster;

import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ClusterDeploymentFailureException;
import com.repoachiever.exception.ClusterDestructionFailureException;
import com.repoachiever.exception.CommandExecutorException;
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
     * @param clusterContext given RepoAchiever Cluster context.
     * @return process identificator of the deployed RepoAchiever Cluster instance.
     * @throws ClusterDeploymentFailureException if deployment operation failed.
     */
    public Integer deploy(String clusterContext) throws ClusterDeploymentFailureException {
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

        return Integer.parseInt(
                clusterDeployCommandOutput.
                        getNormalOutput().
                        replaceAll("\n", ""));
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
}


// TODO: make assignment of random identificators

// TODO: probably move this logic to ClusterService

// TODO: should regenerate topology after each location added