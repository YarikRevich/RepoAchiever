package com.repoachiever.service.element.layout.scene.main.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.alert.ErrorAlert;
import com.repoachiever.service.element.button.BasicButton;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.progressbar.main.deployment.MainDeploymentCircleProgressBar;
import com.repoachiever.service.element.progressbar.main.start.MainStartCircleProgressBar;
import com.repoachiever.service.element.scene.main.deployment.MainDeploymentScene;
import com.repoachiever.service.element.scene.main.start.MainStartScene;
import com.repoachiever.service.element.stage.SettingsStage;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.event.payload.WithdrawEvent;
import com.repoachiever.service.event.state.LocalState;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;

import java.util.UUID;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Represents common menu box.
 */
@Service
public class MainMenuButtonBox implements IElement<VBox> {
    private final UUID id = UUID.randomUUID();

    @Lazy
    @Autowired
    private MainStartScene startScene;

    @Lazy
    @Autowired
    private MainDeploymentScene deploymentScene;

    @Lazy
    @Autowired
    private ErrorAlert errorAlert;

    public MainMenuButtonBox(
            @Autowired PropertiesEntity properties,
            @Autowired MainStartCircleProgressBar mainStartCircleProgressBar) {
        VBox vbox =
                new VBox(
                        20,
                        new BasicButton(
                                "Start",
                                properties,
                                () -> {
                                    ElementHelper.switchScene(getContent().getScene(), startScene.getContent());

                                    ElementHelper.toggleElementVisibility(
                                            mainStartCircleProgressBar.getContent());

                                    SchedulerConfigurationHelper.scheduleTimer(
                                            () ->
                                                    ElementHelper.toggleElementVisibility(
                                                            mainStartCircleProgressBar.getContent()),
                                            properties.getSpinnerInitialDelay());
                                })
                                .getContent(),
                        new BasicButton(
                                "Application",
                                properties,
                                () -> {
                                    if (!ElementHelper.areElementsEqual(
                                            getContent().getScene(), deploymentScene.getContent())) {
                                        ElementHelper.switchScene(
                                                getContent().getScene(), deploymentScene.getContent());
                                    }
                                })
                                .getContent());
        vbox.setPadding(new Insets(10, 0, 10, 0));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setFillWidth(true);

        vbox.setStyle(
                String.format(
                        "-fx-background-color: rgb(%d, %d, %d); " + "-fx-background-radius: 10;",
                        properties.getCommonSceneMenuBackgroundColorR(),
                        properties.getCommonSceneMenuBackgroundColorG(),
                        properties.getCommonSceneMenuBackgroundColorB()));

        ElementStorage.setElement(id, vbox);
    }

    /**
     * @see IElement
     */
    @Override
    public VBox getContent() {
        return ElementStorage.getElement(id);
    }
}
