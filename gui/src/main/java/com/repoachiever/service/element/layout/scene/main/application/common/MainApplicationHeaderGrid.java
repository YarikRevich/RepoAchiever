package com.repoachiever.service.element.layout.scene.main.application.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.main.application.MainApplicationConnectionStatusImageView;
import com.repoachiever.service.element.layout.scene.main.common.MainHeaderGrid;
import org.springframework.stereotype.Service;

/**
 * @see MainHeaderGrid
 */
@Service
public class MainApplicationHeaderGrid extends MainHeaderGrid {
  public MainApplicationHeaderGrid(
      PropertiesEntity properties,
      MainApplicationConnectionStatusImageView mainApplicationConnectionStatusImageView) {
    super(properties, mainApplicationConnectionStatusImageView);
  }
}
