package com.repoachiever.service.element.progressbar.main.start;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.progressbar.common.CircleProgressBar;
import org.springframework.stereotype.Service;

/**
 * @see CircleProgressBar
 */
@Service
public class MainStartCircleProgressBar extends CircleProgressBar {
  public MainStartCircleProgressBar(PropertiesEntity properties) {
    super(properties);
  }
}
