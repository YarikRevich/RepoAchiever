package com.repoachiever.service.element.layout.scene.main.deployment.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.common.MainMenuButtonBox;
import com.repoachiever.service.element.progressbar.main.deployment.MainDeploymentCircleProgressBar;
import com.repoachiever.service.element.progressbar.main.start.MainStartCircleProgressBar;
import com.repoachiever.service.element.stage.SettingsStage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** Represents deployment menu button box used for main stage. */
@Service
public class MainDeploymentMenuButtonBox extends MainMenuButtonBox {
  public MainDeploymentMenuButtonBox(
      PropertiesEntity properties,
      ApplicationEventPublisher applicationEventPublisher,
      SettingsStage settingsStage,
      MainStartCircleProgressBar mainStartCircleProgressBar,
      MainDeploymentCircleProgressBar mainDeploymentCircleProgressBar) {
    super(
        properties,
        applicationEventPublisher,
        settingsStage,
        mainStartCircleProgressBar,
        mainDeploymentCircleProgressBar);
  }
}
