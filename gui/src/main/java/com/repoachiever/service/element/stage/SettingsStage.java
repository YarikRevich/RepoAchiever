package com.repoachiever.service.element.stage;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.progressbar.settings.SettingsCircleProgressBar;
import com.repoachiever.service.element.scene.settings.SettingsGeneralScene;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.scheduler.SchedulerHelper;
import java.util.UUID;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** SettingsStage represents settings window. */
@Service
public class SettingsStage implements IElement<Stage> {
  UUID id = UUID.randomUUID();

  public SettingsStage(
      @Autowired PropertiesEntity properties,
      @Autowired SettingsGeneralScene settingsGeneralScene,
      @Autowired SettingsCircleProgressBar settingsCircleProgressBar,
      @Autowired ApplicationEventPublisher applicationEventPublisher) {
    Platform.runLater(
        () -> {
          Stage settingsStage = new Stage();
          settingsStage.setTitle(properties.getWindowMainName());

          Rectangle2D defaultBounds = Screen.getPrimary().getVisualBounds();

          Rectangle2D window =
              ElementHelper.getSizeWithScale(
                  defaultBounds.getWidth(),
                  defaultBounds.getHeight(),
                  properties.getWindowSettingsScaleWidth(),
                  properties.getWindowSettingsScaleHeight());

          settingsStage.setWidth(window.getWidth());
          settingsStage.setHeight(window.getHeight());
          settingsStage.setMinWidth(window.getWidth());
          settingsStage.setMinHeight(window.getHeight());
          settingsStage.setMaxWidth(window.getWidth());
          settingsStage.setMaxHeight(window.getHeight());

          Point2D centralPoint =
              ElementHelper.getCentralPoint(settingsStage.getWidth(), settingsStage.getHeight());
          settingsStage.setX(centralPoint.getX());
          settingsStage.setY(centralPoint.getY());

          settingsStage.setScene(settingsGeneralScene.getContent());

          settingsStage.setOnShown(
              event -> {
                ElementHelper.toggleElementVisibility(settingsCircleProgressBar.getContent());

                SchedulerHelper.scheduleTimer(
                    () ->
                        ElementHelper.toggleElementVisibility(
                            settingsCircleProgressBar.getContent()),
                    properties.getSpinnerInitialDelay());
              });

          ElementStorage.setElement(id, settingsStage);
        });
  }

  public Stage getContent() {
    return ElementStorage.getElement(id);
  }
}
