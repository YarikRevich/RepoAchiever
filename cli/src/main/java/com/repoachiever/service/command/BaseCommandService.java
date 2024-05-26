package com.repoachiever.service.command;

import com.repoachiever.dto.CleanExternalCommandDto;
import com.repoachiever.dto.DownloadExternalCommandDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.service.command.external.apply.ApplyExternalCommandService;
import com.repoachiever.service.command.external.clean.CleanExternalCommandService;
import com.repoachiever.service.command.external.cleanall.CleanAllExternalCommandService;
import com.repoachiever.service.command.external.content.ContentExternalCommandService;
import com.repoachiever.service.command.external.download.DownloadExternalCommandService;
import com.repoachiever.service.command.external.topology.TopologyExternalCommandService;
import com.repoachiever.service.command.external.version.VersionExternalCommandService;
import com.repoachiever.service.command.external.withdraw.WithdrawExternalCommandService;
import com.repoachiever.service.command.internal.health.HealthCheckInternalCommandService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.VisualizationService;
import com.repoachiever.service.visualization.label.apply.ApplyCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.clean.CleanCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.cleanall.CleanAllCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.content.ContentCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.download.DownloadCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.topology.TopologyCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.withdraw.WithdrawCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.version.VersionCommandVisualizationLabel;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Objects;

/**
 * Represents general command management service.
 */
@Service
@Command(
        name = "help",
        mixinStandardHelpOptions = true,
        description = "Repository achieving tool",
        version = "1.0")
public class BaseCommandService {
    private static final Logger logger = LogManager.getLogger(BaseCommandService.class);

    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ApplyExternalCommandService applyExternalCommandService;

    @Autowired
    private WithdrawExternalCommandService withdrawCommandService;

    @Autowired
    private CleanExternalCommandService cleanCommandService;

    @Autowired
    private CleanAllExternalCommandService cleanAllExternalCommandService;

    @Autowired
    private ContentExternalCommandService contentCommandService;

    @Autowired
    private DownloadExternalCommandService downloadCommandService;

    @Autowired
    private TopologyExternalCommandService topologyCommandService;

    @Autowired
    private VersionExternalCommandService versionCommandService;

    @Autowired
    private HealthCheckInternalCommandService healthCheckInternalCommandService;

    @Autowired
    private ApplyCommandVisualizationLabel applyCommandVisualizationLabel;

    @Autowired
    private WithdrawCommandVisualizationLabel withdrawCommandVisualizationLabel;

    @Autowired
    private CleanCommandVisualizationLabel cleanCommandVisualizationLabel;

    @Autowired
    private CleanAllCommandVisualizationLabel cleanAllCommandVisualizationLabel;

    @Autowired
    private ContentCommandVisualizationLabel contentCommandVisualizationLabel;

    @Autowired
    private DownloadCommandVisualizationLabel downloadCommandVisualizationLabel;

    @Autowired
    private TopologyCommandVisualizationLabel topologyCommandVisualizationLabel;

    @Autowired
    private VersionCommandVisualizationLabel versionCommandVisualizationLabel;

    @Autowired
    private VisualizationService visualizationService;

    @Autowired
    private VisualizationState visualizationState;

    /**
     * Provides access to apply command service.
     */
    @Command(description = "Apply remote configuration")
    private void apply(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(applyCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            applyExternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to withdraw command service.
     */
    @Command(description = "Withdraw remote configuration")
    private void withdraw(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(withdrawCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            withdrawCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to clean command service.
     */
    @Command(description = "Clean remote content")
    private void clean(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation,
            @Option(names = {"--location"}, description = "A name of remote content location", required = true)
            String location) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(cleanCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            cleanCommandService.process(CleanExternalCommandDto.of(configService.getConfig(), location));
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to cleanall command service.
     */
    @Command(description = "Clean all remote content")
    private void cleanAll(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(cleanAllCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            cleanAllExternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to content command service.
     */
    @Command(description = "Retrieve remote content state")
    private void content(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation,
            @Option(names = {"--output"}, description = "", defaultValue = "null") String outputLocation) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(contentCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            contentCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to download command service.
     */
    @Command(description = "Retrieve remote content state")
    private void download(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation,
            @Option(names = {"--output"}, description = "", required = true) String outputLocation,
            @Option(names = {"--location"}, description = "A name of remote content location", required = true)
            String location) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(downloadCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            downloadCommandService.process(DownloadExternalCommandDto.of(configService.getConfig(), outputLocation, location));
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to topology command service.
     */
    @Command(description = "Retrieve topology configuration)")
    private void topology(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(topologyCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            topologyCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }

    /**
     * Provides access to version command service.
     */
    @Command(description = "Retrieve versions of infrastructure)")
    private void version(
            @Option(names = {"--config"}, description = "A location of configuration file", defaultValue = "null")
            String configLocation) {
        if (Objects.equals(configLocation, "null")) {
            configLocation = properties.getConfigDefaultLocation();
        }

        visualizationState.setLabel(versionCommandVisualizationLabel);

        visualizationService.process();

        try {
            configService.configure(configLocation);
        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException | ConfigValidationException |
                 ConfigFileClosureFailureException e) {
            logger.fatal(new ApiServerOperationFailureException(e.getMessage()).getMessage());

            return;
        }

        try {
            healthCheckInternalCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        try {
            versionCommandService.process(configService.getConfig());
        } catch (ApiServerOperationFailureException e) {
            logger.fatal(e.getMessage());

            return;
        }

        visualizationService.await();
    }
}
