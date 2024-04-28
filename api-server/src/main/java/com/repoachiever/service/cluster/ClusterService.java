package com.repoachiever.service.cluster;

import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ClusterDeploymentFailedException;
import com.repoachiever.exception.CommandExecutorException;
import com.repoachiever.exception.DockerIsNotAvailableException;
import com.repoachiever.service.cluster.common.ClusterConfigurationHelper;
import com.repoachiever.service.command.cluster.DeployCommandService;
import com.repoachiever.service.executor.CommandExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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

    // TODO: implement automatic destruction of deployed clusters on the end of API Server execution.
    /**
     * Retrieves amount of allocated workers.
     * @return
     */
    public Integer retrieveWorkerAmount() {
        return 0;
    }

    /**
     * Creates new RepoAchiever Cluster instance.
     *
     * @throws ClusterDeploymentFailedException if deployment operation failed.
     */
    public void createInstance() throws ClusterDeploymentFailedException {
        DeployCommandService deployCommandService =
                new DeployCommandService(
                        properties.getBinDirectory(), properties.getBinClusterName());

        CommandExecutorOutputDto deployCommandOutput;

        try {
            deployCommandOutput =
                    commandExecutorService.executeCommand(deployCommandService);
        } catch (CommandExecutorException e) {
            throw new ClusterDeploymentFailedException(e.getMessage());
        }

        String deployCommandErrorOutput = deployCommandOutput.getErrorOutput();

        System.out.println(deployCommandErrorOutput);
        System.out.println(deployCommandOutput.getNormalOutput());
    }
}


// TODO: make assignment of random identificators

// TODO: probably move this logic to ClusterService

// TODO: should regenerate topology after each location added