package com.repoachiever.service.integration.diagnostics;

import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.CommandExecutorException;
import com.repoachiever.exception.DockerIsNotAvailableException;
import com.repoachiever.exception.DockerNetworkCreateFailureException;
import com.repoachiever.exception.DockerNetworkRemoveFailureException;
import com.repoachiever.service.command.docker.availability.AvailabilityCheckCommandService;
import com.repoachiever.service.command.docker.network.create.CreateCommandService;
import com.repoachiever.service.command.docker.network.remove.RemoveCommandService;
import com.repoachiever.service.command.nodeexporter.DeployCommandService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.executor.CommandExecutorService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Service used to perform diagnostics infrastructure configuration operations.
 */
@Startup
@ApplicationScoped
public class DiagnosticsConfigService {
    private static final Logger logger = LogManager.getLogger(DiagnosticsConfigService.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    @Inject
    CommandExecutorService commandExecutorService;

    @Inject
    AvailabilityCheckCommandService dockerAvailabilityCheckCommandService;

    /**
     * Creates Docker diagnostics network and deploys diagnostics infrastructure instances with pre-defined configurations.
     */
    @PostConstruct
    private void process() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            CommandExecutorOutputDto dockerAvailabilityCommandOutput;

            try {
                dockerAvailabilityCommandOutput =
                        commandExecutorService.executeCommand(dockerAvailabilityCheckCommandService);
            } catch (CommandExecutorException e) {
                logger.fatal(new DockerIsNotAvailableException(e.getMessage()).getMessage());
                return;
            }

            String dockerAvailabilityCommandErrorOutput = dockerAvailabilityCommandOutput.getErrorOutput();

            if ((Objects.nonNull(dockerAvailabilityCommandErrorOutput) &&
                    !dockerAvailabilityCommandErrorOutput.isEmpty()) ||
                    dockerAvailabilityCommandOutput.getNormalOutput().isEmpty()) {
                logger.fatal(new DockerIsNotAvailableException(dockerAvailabilityCommandErrorOutput).getMessage());
                return;
            }

            CreateCommandService networkCreateCommandService =
                    new CreateCommandService(properties.getDiagnosticsCommonDockerNetworkName());

            CommandExecutorOutputDto networkCreateCommandOutput;

            try {
                networkCreateCommandOutput =
                        commandExecutorService.executeCommand(networkCreateCommandService);
            } catch (CommandExecutorException e) {
                logger.fatal(new DockerNetworkCreateFailureException(e.getMessage()).getMessage());
                return;
            }

            String networkCreateCommandErrorOutput = networkCreateCommandOutput.getErrorOutput();

            if (Objects.nonNull(networkCreateCommandErrorOutput) &&
                    !networkCreateCommandErrorOutput.isEmpty()) {
                logger.fatal(new DockerNetworkCreateFailureException(networkCreateCommandErrorOutput).getMessage());
            }

            DeployCommandService nodeExporterDeployCommandService =
                    new DeployCommandService(
                            properties.getDiagnosticsPrometheusNodeExporterDockerName(),
                            properties.getDiagnosticsPrometheusNodeExporterDockerImage(),
                            configService.getConfig().getDiagnostics().getNodeExporter().getPort());

            CommandExecutorOutputDto nodeExporterDeployCommandOutput;

            try {
                nodeExporterDeployCommandOutput =
                        commandExecutorService.executeCommand(nodeExporterDeployCommandService);
            } catch (CommandExecutorException e) {
                logger.fatal(e.getMessage());
                return;
            }

            String nodeExporterDeployCommandErrorOutput = nodeExporterDeployCommandOutput.getErrorOutput();

            if (Objects.nonNull(nodeExporterDeployCommandErrorOutput) &&
                    !nodeExporterDeployCommandErrorOutput.isEmpty()) {
                logger.fatal("fail");
            }
        }
    }

    /**
     * Removes created Docker networks and stops started diagnostics containers.
     */
    @PreDestroy
    private void close() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            CommandExecutorOutputDto dockerAvailabilityCommandOutput;

            try {
                dockerAvailabilityCommandOutput =
                        commandExecutorService.executeCommand(dockerAvailabilityCheckCommandService);
            } catch (CommandExecutorException e) {
                return;
            }

            String dockerAvailabilityCommandErrorOutput = dockerAvailabilityCommandOutput.getErrorOutput();

            if ((Objects.nonNull(dockerAvailabilityCommandErrorOutput) &&
                    !dockerAvailabilityCommandErrorOutput.isEmpty()) ||
                    dockerAvailabilityCommandOutput.getNormalOutput().isEmpty()) {
                return;
            }

            RemoveCommandService networkRemoveCommandService =
                    new RemoveCommandService(properties.getDiagnosticsCommonDockerNetworkName());

            CommandExecutorOutputDto networkRemoveCommandOutput;

            try {
                networkRemoveCommandOutput =
                        commandExecutorService.executeCommand(networkRemoveCommandService);
            } catch (CommandExecutorException e) {
                logger.fatal(new DockerNetworkRemoveFailureException(e.getMessage()).getMessage());
                return;
            }

            String networkRemoveCommandErrorOutput = networkRemoveCommandOutput.getErrorOutput();

            if (Objects.nonNull(networkRemoveCommandErrorOutput) &&
                    !networkRemoveCommandErrorOutput.isEmpty()) {
                logger.fatal(new DockerNetworkRemoveFailureException(networkRemoveCommandErrorOutput).getMessage());
            }
        }
    }
}
