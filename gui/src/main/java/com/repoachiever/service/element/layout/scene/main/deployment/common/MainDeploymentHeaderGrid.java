package com.repoachiever.service.element.layout.scene.main.deployment.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.main.deployment.MainDeploymentConnectionStatusImageView;
import com.repoachiever.service.element.layout.scene.main.common.MainHeaderGrid;
import org.springframework.stereotype.Service;

/**
 * @see MainHeaderGrid
 */
@Service
public class MainDeploymentHeaderGrid extends MainHeaderGrid {
  public MainDeploymentHeaderGrid(
      PropertiesEntity properties,
      MainDeploymentConnectionStatusImageView mainDeploymentConnectionStatusImageView) {
    super(properties, mainDeploymentConnectionStatusImageView);
  }
}
