package com.repoachiever.service.element.image.view.main.start;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.common.ConnectionStatusImageView;
import org.springframework.stereotype.Service;

/**
 * @see ConnectionStatusImageView
 */
@Service
public class MainStartConnectionStatusImageView extends ConnectionStatusImageView {
  public MainStartConnectionStatusImageView(PropertiesEntity properties) {
    super(properties);
  }
}
