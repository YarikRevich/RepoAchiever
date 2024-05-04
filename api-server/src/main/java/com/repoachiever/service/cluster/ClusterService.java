package com.repoachiever.service.cluster;

import com.repoachiever.converter.ClusterContextToJsonConverter;
import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.entity.common.ConfigEntity;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ClusterDeploymentFailureException;
import com.repoachiever.exception.ClusterDestructionFailureException;
import com.repoachiever.exception.CommandExecutorException;
import com.repoachiever.service.command.cluster.deploy.DeployCommandService;
import com.repoachiever.service.command.cluster.destroy.DestroyCommandService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.executor.CommandExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        DeployCommandService deployCommandService =
                new DeployCommandService(
                        clusterContext,
                        properties.getBinDirectory(),
                        properties.getBinClusterLocation());

        CommandExecutorOutputDto deployCommandOutput;

        try {
            deployCommandOutput =
                    commandExecutorService.executeCommand(deployCommandService);
        } catch (CommandExecutorException e) {
            throw new ClusterDeploymentFailureException(e.getMessage());
        }

        String deployCommandErrorOutput = deployCommandOutput.getErrorOutput();

        if (Objects.nonNull(deployCommandErrorOutput) && !deployCommandErrorOutput.isEmpty()) {
            throw new ClusterDeploymentFailureException();
        }

        return Integer.parseInt(
                deployCommandOutput.
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
        DestroyCommandService destroyCommandService = new DestroyCommandService(pid);

        CommandExecutorOutputDto destroyCommandOutput;

        try {
            destroyCommandOutput =
                    commandExecutorService.executeCommand(destroyCommandService);
        } catch (CommandExecutorException e) {
            throw new ClusterDestructionFailureException(e.getMessage());
        }

        String destroyCommandErrorOutput = destroyCommandOutput.getErrorOutput();

        if (Objects.nonNull(destroyCommandErrorOutput) && !destroyCommandErrorOutput.isEmpty()) {
            throw new ClusterDestructionFailureException();
        }
    }
}


// TODO: make assignment of random identificators

// TODO: probably move this logic to ClusterService

// TODO: should regenerate topology after each location added