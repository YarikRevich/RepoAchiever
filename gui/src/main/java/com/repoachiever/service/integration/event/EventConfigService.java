package com.repoachiever.service.integration.event;

import com.repoachiever.converter.ContentRetrievalUnitToJsonConverter;
import com.repoachiever.converter.TopologyInfoUnitsToJsonConverter;
import com.repoachiever.dto.CleanExternalCommandDto;
import com.repoachiever.dto.DownloadExternalCommandDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.command.config.open.OpenConfigCommandService;
import com.repoachiever.service.command.swap.open.OpenSwapCommandService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.element.alert.ErrorAlert;
import com.repoachiever.service.element.alert.InformationAlert;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.progressbar.main.deployment.MainDeploymentCircleProgressBar;
import com.repoachiever.service.event.payload.*;
import com.repoachiever.service.executor.CommandExecutorService;
import com.repoachiever.service.hand.external.apply.ApplyExternalCommandService;
import com.repoachiever.service.hand.external.clean.CleanExternalCommandService;
import com.repoachiever.service.hand.external.cleanall.CleanAllExternalCommandService;
import com.repoachiever.service.hand.external.content.ContentExternalCommandService;
import com.repoachiever.service.hand.external.download.DownloadExternalCommandService;
import com.repoachiever.service.hand.external.topology.TopologyExternalCommandService;
import com.repoachiever.service.hand.external.version.VersionExternalCommandService;
import com.repoachiever.service.hand.external.withdraw.WithdrawExternalCommandService;
import com.repoachiever.service.hand.internal.health.HealthCheckInternalCommandService;
import com.repoachiever.service.integration.event.common.EventConfigurationHelper;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;
import com.repoachiever.service.state.StateService;
import jakarta.annotation.PostConstruct;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import process.SProcessExecutor;

/**
 * Service used to perform RepoAchiever GUI event handling
 */
@EnableAsync
@Component
public class EventConfigService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MainDeploymentCircleProgressBar mainDeploymentCircleProgressBar;

    @Autowired
    private CommandExecutorService commandExecutorService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ApplyExternalCommandService applyExternalCommandService;

    @Autowired
    private WithdrawExternalCommandService withdrawExternalCommandService;

    @Autowired
    private CleanExternalCommandService cleanExternalCommandService;

    @Autowired
    private CleanAllExternalCommandService cleanAllExternalCommandService;

    @Autowired
    private ContentExternalCommandService contentExternalCommandService;

    @Autowired
    private DownloadExternalCommandService downloadExternalCommandService;

    @Autowired
    private TopologyExternalCommandService topologyExternalCommandService;

    @Autowired
    private VersionExternalCommandService versionExternalCommandService;

    @Autowired
    private HealthCheckInternalCommandService healthCheckInternalCommandService;

    @Autowired
    private InformationAlert informationAlert;

    @Autowired
    private ErrorAlert errorAlert;

    @Autowired
    private EventConfigurationHelper eventConfigurationHelper;

    /**
     * Attempts to perform configuration setup.
     */
    @PostConstruct
    private void configure() {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    try {
                        StateService.getStartupGuard().await();
                    } catch (InterruptedException e) {
                        ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        try {
                            configService.configure(new File(properties.getConfigDefaultLocation()));
                        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException |
                                 ConfigValidationException | ConfigFileClosureFailureException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new DefaultConfigurationFailureException(e.getMessage()).getMessage());

                            return;
                        }

                        VersionInfoResult versionInfoResult;

                        try {
                            versionInfoResult =
                                    versionExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                            return;
                        }

                        StateService.setConfigLocation(new File(properties.getConfigDefaultLocation()));

                        ContentRetrievalResult contentRetrievalResult;

                        try {
                            contentRetrievalResult =
                                    contentExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!contentRetrievalResult.getLocations().isEmpty()) {
                            StateService.setContent(contentRetrievalResult);
                        } else {
                            StateService.setContent(null);
                        }
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Provides initial window resolution setup.
     *
     * @param contextRefreshedEvent embedded context refreshed event.
     */
    @EventListener(classes = {ContextRefreshedEvent.class})
    private void eventListener(ContextRefreshedEvent contextRefreshedEvent) {
        Rectangle2D defaultBounds = Screen.getPrimary().getVisualBounds();

        Rectangle2D window =
                ElementHelper.getSizeWithScale(
                        defaultBounds.getWidth(),
                        defaultBounds.getHeight(),
                        properties.getWindowMainScaleMinWidth(),
                        properties.getWindowMainScaleMinHeight());

        applicationEventPublisher.publishEvent(new MainWindowWidthUpdateEvent(window.getWidth()));
        applicationEventPublisher.publishEvent(new MainWindowHeightUpdateEvent(window.getHeight()));

        StateService.getStartupGuard().countDown();
    }

    /**
     * Handles incoming health check event.
     *
     * @param event given health check event.
     */
    @EventListener
    private void handleHealthCheckEvent(HealthCheckEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    HealthCheckResult healthCheckResult;

                    try {
                        healthCheckResult =
                                healthCheckInternalCommandService.process(configService.getConfig());
                    } catch (ApiServerOperationFailureException e) {
                        StateService.setConnectionEstablished(false);

                        return;
                    }

                    switch (healthCheckResult.getStatus()) {
                        case UP -> StateService.setConnectionEstablished(true);
                        case DOWN -> StateService.setConnectionEstablished(false);
                    }
                });
    }

    /**
     * Handles incoming apply event.
     *
     * @param event given apply event.
     */
    @EventListener
    private void handleApplyEvent(ApplyEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    if (Objects.isNull(StateService.getConfigLocation())) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertConfigNotOpenedMessage());

                        return;
                    }

                    if (!StateService.getConnectionEstablished()) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertApiServerUnavailableMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        VersionInfoResult versionInfoResult;

                        try {
                            versionInfoResult =
                                    versionExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                            return;
                        }

                        try {
                            applyExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        ElementHelper.showAlert(
                                informationAlert.getContent(), properties.getAlertApplicationFinishedMessage());
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles incoming withdraw event.
     *
     * @param event given withdraw event.
     */
    @EventListener
    private void handleWithdrawEvent(WithdrawEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    if (Objects.isNull(StateService.getConfigLocation())) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertConfigNotOpenedMessage());

                        return;
                    }

                    if (!StateService.getConnectionEstablished()) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertApiServerUnavailableMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        VersionInfoResult versionInfoResult;

                        try {
                            versionInfoResult =
                                    versionExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                            return;
                        }

                        try {
                            withdrawExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        ContentRetrievalResult contentRetrievalResult;

                        try {
                            contentRetrievalResult =
                                    contentExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!contentRetrievalResult.getLocations().isEmpty()) {
                            StateService.setContent(contentRetrievalResult);
                        } else {
                            StateService.setContent(null);
                        }

                        ElementHelper.showAlert(
                                informationAlert.getContent(), properties.getAlertWithdrawalFinishedMessage());
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles incoming retrieve content event.
     *
     * @param event given retrieve content event.
     */
    @EventListener
    private void handleRetrieveContentEvent(RetrieveContentEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    if (Objects.isNull(StateService.getConfigLocation())) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertConfigNotOpenedMessage());

                        return;
                    }

                    if (!StateService.getConnectionEstablished()) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertApiServerUnavailableMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        VersionInfoResult versionInfoResult;

                        try {
                            versionInfoResult =
                                    versionExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                            return;
                        }

                        ContentRetrievalResult contentRetrievalResult;

                        try {
                            contentRetrievalResult =
                                    contentExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!contentRetrievalResult.getLocations().isEmpty()) {
                            StateService.setContent(contentRetrievalResult);
                        } else {
                            StateService.setContent(null);
                        }
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles incoming download event.
     *
     * @param event given download event.
     */
    @EventListener
    private void handleDownloadEvent(DownloadEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(() -> {
            if (!StateService.getConnectionEstablished()) {
                ElementHelper.showAlert(
                        errorAlert.getContent(),
                        properties.getAlertApiServerUnavailableMessage());

                return;
            }

            ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

            try {
                VersionInfoResult versionInfoResult;

                try {
                    versionInfoResult =
                            versionExternalCommandService.process(configService.getConfig());
                } catch (ApiServerOperationFailureException e) {
                    ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                    return;
                }

                if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                    ElementHelper.showAlert(
                            errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                    return;
                }

                byte[] contentDownloadResult;

                try {
                    contentDownloadResult = downloadExternalCommandService.process(
                            DownloadExternalCommandDto.of(configService.getConfig(), event.getLocation()));
                } catch (ApiServerOperationFailureException e) {
                    ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                    return;
                }

                try {
                    FileUtils.writeByteArrayToFile(
                            new File(
                                    eventConfigurationHelper.getOutputLocation(
                                            Path.of(event.getDestination().getPath(), event.getLocation()).toString())),
                            contentDownloadResult);
                } catch (IOException e) {
                    ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                }

                ElementHelper.showAlert(
                        informationAlert.getContent(), properties.getAlertDownloadFinishedMessage());
            } finally {
                ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
            }
        });
    }

    /**
     * Handles incoming clean event.
     *
     * @param event given clean event.
     */
    @EventListener
    private void handleCleanEvent(CleanEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(() -> {
            if (!StateService.getConnectionEstablished()) {
                ElementHelper.showAlert(
                        errorAlert.getContent(),
                        properties.getAlertApiServerUnavailableMessage());

                return;
            }

            ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

            try {
                try {
                    cleanExternalCommandService.process(
                            CleanExternalCommandDto.of(configService.getConfig(), event.getLocation()));
                } catch (ApiServerOperationFailureException e) {
                    ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                }

                ContentRetrievalResult contentRetrievalResult;

                try {
                    contentRetrievalResult =
                            contentExternalCommandService.process(configService.getConfig());
                } catch (ApiServerOperationFailureException e) {
                    ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                    return;
                }

                if (!contentRetrievalResult.getLocations().isEmpty()) {
                    StateService.setContent(contentRetrievalResult);
                } else {
                    StateService.setContent(null);
                }
            } finally {
                ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
            }
        });
    }

    /**
     * Handles incoming cleanall event.
     *
     * @param event given cleanall event.
     */
    @EventListener
    private void handleCleanAllEvent(CleanAllEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    if (Objects.isNull(StateService.getConfigLocation())) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertConfigNotOpenedMessage());

                        return;
                    }

                    if (!StateService.getConnectionEstablished()) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertApiServerUnavailableMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        VersionInfoResult versionInfoResult;

                        try {
                            versionInfoResult =
                                    versionExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                            return;
                        }

                        try {
                            cleanAllExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                        }

                        ContentRetrievalResult contentRetrievalResult;

                        try {
                            contentRetrievalResult =
                                    contentExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!contentRetrievalResult.getLocations().isEmpty()) {
                            StateService.setContent(contentRetrievalResult);
                        } else {
                            StateService.setContent(null);
                        }
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles incoming edit event.
     *
     * @param event given edit event.
     */
    @EventListener
    private void handleEditEvent(EditEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    if (Objects.isNull(StateService.getConfigLocation())) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertConfigNotOpenedMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        OpenConfigCommandService openConfigCommandService =
                                new OpenConfigCommandService(StateService.getConfigLocation().getPath());

                        if (commandExecutorService.getOSType() == SProcessExecutor.OS.MAC) {
                            ElementHelper.showAlert(
                                    informationAlert.getContent(), properties.getAlertEditorCloseReminderMessage());
                        }

                        try {
                            commandExecutorService.executeCommand(openConfigCommandService);
                        } catch (CommandExecutorException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        try {
                            configService.configure(StateService.getConfigLocation());
                        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException |
                                 ConfigValidationException | ConfigFileClosureFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                        }
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles incoming open event.
     *
     * @param event given open event.
     */
    @EventListener
    private void handleOpenEvent(OpenEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        try {
                            configService.configure(event.getConfigLocation());
                        } catch (ConfigFileNotFoundException | ConfigFileReadingFailureException |
                                 ConfigValidationException | ConfigFileClosureFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        VersionInfoResult versionInfoResult;

                        try {
                            versionInfoResult =
                                    versionExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());

                            return;
                        }

                        StateService.setConfigLocation(event.getConfigLocation());

                        ContentRetrievalResult contentRetrievalResult;

                        try {
                            contentRetrievalResult =
                                    contentExternalCommandService.process(configService.getConfig());
                        } catch (ApiServerOperationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                            return;
                        }

                        if (!contentRetrievalResult.getLocations().isEmpty()) {
                            StateService.setContent(contentRetrievalResult);
                        } else {
                            StateService.setContent(null);
                        }
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles incoming topology swap event.
     *
     * @param event given topology swap event.
     */
    @EventListener
    private void handleTopologySwapEvent(TopologySwapEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    if (Objects.isNull(StateService.getConfigLocation())) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertConfigNotOpenedMessage());

                        return;
                    }

                    if (!StateService.getConnectionEstablished()) {
                        ElementHelper.showAlert(
                                errorAlert.getContent(),
                                properties.getAlertApiServerUnavailableMessage());

                        return;
                    }

                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    List<TopologyInfoUnit> topologyInfoUnits;

                    try {
                        topologyInfoUnits = topologyExternalCommandService.process(configService.getConfig());
                    } catch (ApiServerOperationFailureException e) {
                        ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());

                        return;
                    }

                    String swapLocation = eventConfigurationHelper.getSwapLocation();

                    try {
                        String topologyInfoUnitsRaw;

                        try {
                            topologyInfoUnitsRaw =
                                    TopologyInfoUnitsToJsonConverter.convert(topologyInfoUnits);
                        } catch (TopologyInfoUnitsToJsonConversionFailureException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new SwapFileCreationFailureException(e.getMessage()).getMessage());

                            return;
                        }

                        try {
                            Files.writeString(Path.of(swapLocation), topologyInfoUnitsRaw);
                        } catch (IOException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new SwapFileCreationFailureException(e.getMessage()).getMessage());

                            return;
                        }

                        OpenSwapCommandService openSwapCommandService = new OpenSwapCommandService(swapLocation);

                        if (commandExecutorService.getOSType() == SProcessExecutor.OS.MAC) {
                            ElementHelper.showAlert(
                                    informationAlert.getContent(), properties.getAlertEditorCloseReminderMessage());
                        }

                        try {
                            commandExecutorService.executeCommand(openSwapCommandService);
                        } catch (CommandExecutorException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                        }
                    } finally {
                        try {
                            Files.deleteIfExists(Paths.get(swapLocation));
                        } catch (IOException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new SwapFileDeletionFailureException(e.getMessage()).getMessage());
                        } finally {
                            ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                        }
                    }
                });
    }

    /**
     * Handles incoming details swap event.
     *
     * @param event given details swap event.
     */
    @EventListener
    private void handleDetailsSwapEvent(ContentSwapEvent event) {
        SchedulerConfigurationHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    String swapLocation = eventConfigurationHelper.getSwapLocation();

                    try {
                        String contentRetrievalUnitRaw;

                        try {
                            contentRetrievalUnitRaw =
                                    ContentRetrievalUnitToJsonConverter.convert(event.getContent());
                        } catch (ContentRetrievalUnitToJsonConversionFailureException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new SwapFileCreationFailureException(e.getMessage()).getMessage());

                            return;
                        }

                        try {
                            Files.writeString(Path.of(swapLocation), contentRetrievalUnitRaw);
                        } catch (IOException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new SwapFileCreationFailureException(e.getMessage()).getMessage());

                            return;
                        }

                        OpenSwapCommandService openSwapCommandService = new OpenSwapCommandService(swapLocation);

                        if (commandExecutorService.getOSType() == SProcessExecutor.OS.MAC) {
                            ElementHelper.showAlert(
                                    informationAlert.getContent(), properties.getAlertEditorCloseReminderMessage());
                        }

                        try {
                            commandExecutorService.executeCommand(openSwapCommandService);
                        } catch (CommandExecutorException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                        }
                    } finally {
                        try {
                            Files.deleteIfExists(Paths.get(swapLocation));
                        } catch (IOException e) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(),
                                    new SwapFileDeletionFailureException(e.getMessage()).getMessage());
                        } finally {
                            ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                        }
                    }
                });
    }

    /**
     * Handles changes of main window height update.
     *
     * @param event main window height update event, which contains new window height.
     */
    @EventListener
    private void handleMainWindowHeightUpdateEvent(MainWindowHeightUpdateEvent event) {
        StateService.setMainWindowHeight(event.getHeight());

        if (StateService.getMainWindowHeightUpdateMutex().getCount() > 0) {
            StateService.getMainWindowHeightUpdateMutex().countDown();
        }
    }

    /**
     * Handles changes of main window width update.
     *
     * @param event main window width update event, which contains new window width.
     */
    @EventListener
    private void handleMainWindowWidthUpdateEvent(MainWindowWidthUpdateEvent event) {
        StateService.setMainWindowWidth(event.getWidth());

        if (StateService.getMainWindowWidthUpdateMutex().getCount() > 0) {
            StateService.getMainWindowWidthUpdateMutex().countDown();
        }
    }
}
