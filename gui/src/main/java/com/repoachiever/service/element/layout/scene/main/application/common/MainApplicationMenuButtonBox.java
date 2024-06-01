package com.repoachiever.service.element.layout.scene.main.application.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.common.MainMenuButtonBox;
import com.repoachiever.service.element.progressbar.main.start.MainStartCircleProgressBar;
import org.springframework.stereotype.Service;

/** Represents main application menu button box used for global menu visualization. */
@Service
public class MainApplicationMenuButtonBox extends MainMenuButtonBox {
  public MainApplicationMenuButtonBox(PropertiesEntity properties, MainStartCircleProgressBar mainStartCircleProgressBar) {
    super(properties, mainStartCircleProgressBar);
  }
}
