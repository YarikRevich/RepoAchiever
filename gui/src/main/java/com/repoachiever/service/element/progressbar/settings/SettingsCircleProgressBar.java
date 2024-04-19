package com.repoachiever.service.element.progressbar.settings;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.progressbar.common.CircleProgressBar;
import org.springframework.stereotype.Service;

/**
 * @see CircleProgressBar
 */
@Service
public class SettingsCircleProgressBar extends CircleProgressBar {
  public SettingsCircleProgressBar(PropertiesEntity properties) {
    super(properties);
  }
}
