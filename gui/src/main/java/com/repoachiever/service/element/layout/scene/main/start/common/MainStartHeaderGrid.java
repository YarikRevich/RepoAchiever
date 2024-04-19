package com.repoachiever.service.element.layout.scene.main.start.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.main.start.MainStartConnectionStatusImageView;
import com.repoachiever.service.element.layout.scene.main.common.MainHeaderGrid;
import org.springframework.stereotype.Service;

/**
 * @see MainHeaderGrid
 */
@Service
public class MainStartHeaderGrid extends MainHeaderGrid {
  public MainStartHeaderGrid(
      PropertiesEntity properties,
      MainStartConnectionStatusImageView mainStartConnectionStatusImageView) {
    super(properties, mainStartConnectionStatusImageView);
  }
}
