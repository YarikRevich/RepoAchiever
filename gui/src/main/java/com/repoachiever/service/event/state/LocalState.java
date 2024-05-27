package com.repoachiever.service.event.state;

import com.repoachiever.dto.*;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.CommandExecutorException;
import com.repoachiever.exception.SwapFileCreationFailureException;
import com.repoachiever.exception.SwapFileDeletionFailureException;
import com.repoachiever.model.TopicLogsResult;
import com.repoachiever.service.client.command.*;
import com.repoachiever.service.command.external.version.VersionExternalCommandService;
import com.repoachiever.service.command.internal.health.HealthCheckInternalCommandService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.element.alert.ErrorAlert;
import com.repoachiever.service.element.alert.InformationAlert;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.progressbar.main.deployment.MainDeploymentCircleProgressBar;
import com.repoachiever.service.event.payload.*;
import com.repoachiever.service.hand.config.command.OpenConfigEditorCommandService;
import com.repoachiever.service.hand.config.command.OpenSwapFileEditorCommandService;
import com.repoachiever.service.hand.executor.CommandExecutorService;
import com.repoachiever.service.scheduler.SchedulerHelper;
import com.repoachiever.service.swap.SwapService;

import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import process.SProcessExecutor;

/**
 * Represents local state management model, which is used to handle application state changes and
 * exposes them for the further usage.
 */
@EnableAsync
@Component
public class LocalState {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CommandExecutorService commandExecutorService;

    @Autowired
    private SwapService swapService;

    @Autowired
    private MainDeploymentCircleProgressBar mainDeploymentCircleProgressBar;

    @Autowired
    private ApplyClientCommandService applyClientCommandService;

    @Autowired
    private DestroyClientCommandService destroyClientCommandService;

    @Autowired
    private LogsClientCommandService logsClientCommandService;

    @Autowired
    private HealthCheckClientCommandService healthCheckClientCommandService;

    @Autowired
    private ReadinessCheckClientCommandService readinessCheckClientCommandService;

    @Autowired
    private ScriptAcquireClientCommandService scriptAcquireClientCommandService;

    @Autowired
    private SecretsAcquireClientCommandService secretsAcquireClientCommandService;

    @Autowired
    private VersionClientCommandService versionClientCommandService;

    @Autowired
    private VersionExternalCommandService versionExternalCommandService;

    @Autowired
    private HealthCheckInternalCommandService healthCheckInternalCommandService;

    @Autowired
    private ReadinessCheckInternalCommandService readinessCheckInternalCommandService;

    @Autowired
    private StartExternalCommandService startExternalCommandService;

    @Autowired
    private StopExternalCommandService stopExternalCommandService;

    @Autowired
    private StateExternalCommandService stateExternalCommandService;

    @Autowired
    private InformationAlert informationAlert;

    @Autowired
    private ErrorAlert errorAlert;

    @Getter
    @Setter
    private static Boolean connectionEstablished = false;

    @Getter
    @Setter
    private static Object deploymentState;

    @Getter
    @Setter
    private static Double prevMainWindowHeight;

    @Getter
    @Setter
    private static Double mainWindowHeight;

    @Getter
    @Setter
    private static Double prevMainWindowWidth;

    @Getter
    @Setter
    private static Double mainWindowWidth;

    private static final CountDownLatch mainWindowWidthUpdateMutex = new CountDownLatch(1);

    private static final CountDownLatch mainWindowHeightUpdateMutex = new CountDownLatch(1);

    /**
     * Checks if window height has changed.
     *
     * @return result of the check.
     */
    @SneakyThrows
    public static synchronized Boolean isWindowHeightChanged() {
        if (Objects.isNull(LocalState.getPrevMainWindowHeight())
                && !Objects.isNull(LocalState.getMainWindowHeight())) {
            return true;
        } else if (Objects.isNull(LocalState.getPrevMainWindowHeight())) {
            mainWindowHeightUpdateMutex.await();

            return false;
        }

        return !prevMainWindowHeight.equals(mainWindowHeight);
    }

    /**
     * Checks if window width has changed.
     *
     * @return result of the check.
     */
    @SneakyThrows
    public static synchronized Boolean isWindowWidthChanged() {
        if (Objects.isNull(LocalState.getPrevMainWindowWidth())
                && !Objects.isNull(LocalState.getMainWindowWidth())) {
            return true;
        } else if (Objects.isNull(LocalState.getPrevMainWindowWidth())) {
            mainWindowWidthUpdateMutex.await();

            return false;
        }

        return !prevMainWindowWidth.equals(mainWindowWidth);
    }

    /**
     * Synchronizes main window height.
     */
    public static synchronized void synchronizeWindowHeight() {
        LocalState.setPrevMainWindowHeight(LocalState.getMainWindowHeight());
    }

    /**
     * Synchronizes main window width.
     */
    public static synchronized void synchronizeWindowWidth() {
        LocalState.setPrevMainWindowWidth(LocalState.getMainWindowWidth());
    }

    /**
     * Provides initial window resolution setup.
     *
     * @param contextRefreshedEvent embedded context refreshed event.
     */
    @EventListener(classes = {ContextRefreshedEvent.class})
    public void eventListener(ContextRefreshedEvent contextRefreshedEvent) {
        Rectangle2D defaultBounds = Screen.getPrimary().getVisualBounds();

        Rectangle2D window =
                ElementHelper.getSizeWithScale(
                        defaultBounds.getWidth(),
                        defaultBounds.getHeight(),
                        properties.getWindowMainScaleMinWidth(),
                        properties.getWindowMainScaleMinHeight());

        applicationEventPublisher.publishEvent(new MainWindowWidthUpdateEvent(window.getWidth()));
        applicationEventPublisher.publishEvent(new MainWindowHeightUpdateEvent(window.getHeight()));
    }

    /**
     * Handles changes of connection establishment status.
     *
     * @param event connection status event, which contains connection establishment status.
     */
    @EventListener
    public void handleConnectionStatusEvent(ConnectionStatusEvent event) {
        LocalState.setConnectionEstablished(event.isConnectionEstablished());
    }

    /**
     * Handles changes of start deployment status.
     *
     * @param event deployment start event.
     */
    @EventListener
    public void handleStartDeploymentEvent(StartDeploymentEvent event) {
        SchedulerHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        HealthCheckInternalCommandResultDto healthCheckInternalCommandResultDto =
                                healthCheckInternalCommandService.process();

                        if (!healthCheckInternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), healthCheckInternalCommandResultDto.getError());
                            return;
                        }

                        VersionExternalCommandResultDto versionExternalCommandResultDto =
                                versionExternalCommandService.process();

                        if (!versionExternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), versionExternalCommandResultDto.getError());
                            return;
                        }

                        if (!versionExternalCommandResultDto.getData().equals(properties.getGitCommitId())) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), properties.getAlertVersionMismatchMessage());
                            return;
                        }

                        StartExternalCommandResultDto startExternalCommandResultDto =
                                startExternalCommandService.process();

                        if (!startExternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), startExternalCommandResultDto.getError());
                            return;
                        }

                        ElementHelper.showAlert(
                                informationAlert.getContent(), properties.getAlertDeploymentFinishedMessage());
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles changes of stop deployment status.
     *
     * @param event deployment stop event.
     */
    @EventListener
    public void handleStopDeploymentEvent(StopDeploymentEvent event) {
        SchedulerHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        HealthCheckInternalCommandResultDto healthCheckInternalCommandResultDto =
                                healthCheckInternalCommandService.process();

                        if (!healthCheckInternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), healthCheckInternalCommandResultDto.getError());
                            return;
                        }

                        StopExternalCommandResultDto stopExternalCommandResultDto =
                                stopExternalCommandService.process();

                        if (!stopExternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), stopExternalCommandResultDto.getError());
                            return;
                        }

                        LocalState.setDeploymentState(null);

                        ElementHelper.showAlert(
                                informationAlert.getContent(), properties.getAlertDestructionFinishedMessage());
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles changes of deployment state.
     *
     * @param event deployment status retrieval event.
     */
    @EventListener
    public void handleDeploymentStateRetrievalEvent(DeploymentStateRetrievalEvent event) {
        SchedulerHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        HealthCheckInternalCommandResultDto healthCheckInternalCommandResultDto =
                                healthCheckInternalCommandService.process();

                        if (!healthCheckInternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), healthCheckInternalCommandResultDto.getError());
                            return;
                        }

                        ReadinessCheckInternalCommandResultDto readinessCheckInternalCommandResultDto =
                                readinessCheckInternalCommandService.process();

                        if (!readinessCheckInternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), readinessCheckInternalCommandResultDto.getError());
                            return;
                        }

                        StateExternalCommandResultDto stateExternalCommandResultDto =
                                stateExternalCommandService.process();

                        if (!stateExternalCommandResultDto.getStatus()) {
                            ElementHelper.showAlert(
                                    errorAlert.getContent(), stateExternalCommandResultDto.getError());
                            return;
                        }

                        LocalState.setDeploymentState(stateExternalCommandResultDto.getTopicLogsResult());
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles changes of main window height update.
     *
     * @param event main window height update event, which contains new window height.
     */
    @EventListener
    synchronized void handleMainWindowHeightUpdateEvent(MainWindowHeightUpdateEvent event) {
        LocalState.setMainWindowHeight(event.getHeight());

        mainWindowHeightUpdateMutex.countDown();
    }

    /**
     * Handles changes of main window width update.
     *
     * @param event main window width update event, which contains new window width.
     */
    @EventListener
    synchronized void handleMainWindowWidthUpdateEvent(MainWindowWidthUpdateEvent event) {
        LocalState.setMainWindowWidth(event.getWidth());

        mainWindowWidthUpdateMutex.countDown();
    }

    /**
     * Handles requests to open editor window.
     *
     * @param event editor open window event.
     */
    @EventListener
    synchronized void handleEditorOpenWindowEvent(EditorOpenWindowEvent event) {
        SchedulerHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    try {
                        OpenConfigEditorCommandService openConfigEditorCommandService =
                                new OpenConfigEditorCommandService(
                                        properties.getConfigRootPath(), properties.getConfigUserFilePath());

                        if (commandExecutorService.getOSType() == SProcessExecutor.OS.MAC) {
                            ElementHelper.showAlert(
                                    informationAlert.getContent(), properties.getAlertEditorCloseReminderMessage());
                        }

                        try {
                            commandExecutorService.executeCommand(openConfigEditorCommandService);
                        } catch (CommandExecutorException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                            return;
                        }

                        configService.configure();
                        applyClientCommandService.configure();
                        destroyClientCommandService.configure();
                        logsClientCommandService.configure();
                        healthCheckClientCommandService.configure();
                        readinessCheckClientCommandService.configure();
                        scriptAcquireClientCommandService.configure();
                        secretsAcquireClientCommandService.configure();
                        versionClientCommandService.configure();
                    } finally {
                        ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                    }
                });
    }

    /**
     * Handles requests to open swap file window.
     *
     * @param event swap file open window event.
     */
    @EventListener
    synchronized void handleSwapFileOpenWindowEvent(SwapFileOpenWindowEvent event) {
        SchedulerHelper.scheduleOnce(
                () -> {
                    ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());

                    String swapFilePath = null;
                    try {
                        try {
                            swapFilePath =
                                    swapService.createSwapFile(
                                            Paths.get(System.getProperty("user.home"), properties.getSwapRootPath())
                                                    .toString(),
                                            event.getContent());
                        } catch (SwapFileCreationFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                            return;
                        }

                        OpenSwapFileEditorCommandService openSwapFileEditorCommandService =
                                new OpenSwapFileEditorCommandService(swapFilePath);

                        if (commandExecutorService.getOSType() == SProcessExecutor.OS.MAC) {
                            ElementHelper.showAlert(
                                    informationAlert.getContent(), properties.getAlertEditorCloseReminderMessage());
                        }

                        try {
                            commandExecutorService.executeCommand(openSwapFileEditorCommandService);
                        } catch (CommandExecutorException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                        }
                    } finally {
                        try {
                            swapService.deleteSwapFile(swapFilePath);
                        } catch (SwapFileDeletionFailureException e) {
                            ElementHelper.showAlert(errorAlert.getContent(), e.getMessage());
                        } finally {
                            ElementHelper.toggleElementVisibility(mainDeploymentCircleProgressBar.getContent());
                        }
                    }
                });
    }
}
