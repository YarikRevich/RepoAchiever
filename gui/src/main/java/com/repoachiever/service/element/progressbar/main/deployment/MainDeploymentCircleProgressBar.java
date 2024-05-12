package com.repoachiever.service.element.progressbar.main.deployment;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.progressbar.common.CircleProgressBar;
import org.springframework.stereotype.Service;

/**
 * @see CircleProgressBar
 */
@Service
public class MainDeploymentCircleProgressBar extends CircleProgressBar {
  public MainDeploymentCircleProgressBar(PropertiesEntity properties) {
    super(properties);
  }
}
