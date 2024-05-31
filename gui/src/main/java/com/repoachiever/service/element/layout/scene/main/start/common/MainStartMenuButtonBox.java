package com.repoachiever.service.element.layout.scene.main.start.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.common.MainMenuButtonBox;
import com.repoachiever.service.element.progressbar.main.deployment.MainDeploymentCircleProgressBar;
import com.repoachiever.service.element.progressbar.main.start.MainStartCircleProgressBar;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** Represents main start menu button box used for global menu visualization. */
@Service
public class MainStartMenuButtonBox extends MainMenuButtonBox {
  public MainStartMenuButtonBox(PropertiesEntity properties, MainStartCircleProgressBar mainStartCircleProgressBar) {
    super(properties, mainStartCircleProgressBar);
  }
}
