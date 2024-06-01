package com.repoachiever.service.element.image.view.main.application;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.common.ConnectionStatusImageView;
import org.springframework.stereotype.Service;

/**
 * @see ConnectionStatusImageView
 */
@Service
public class MainApplicationConnectionStatusImageView extends ConnectionStatusImageView {
  public MainApplicationConnectionStatusImageView(PropertiesEntity properties) {
    super(properties);
  }
}
