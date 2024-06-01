package com.repoachiever.service.element.layout.scene.main.application.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.common.MainFooterGrid;
import com.repoachiever.service.element.text.main.deployment.MainDeploymentBuildVersionText;
import org.springframework.stereotype.Service;

/**
 * @see MainFooterGrid
 */
@Service
public class MainApplicationFooterGrid extends MainFooterGrid {
  public MainApplicationFooterGrid(
      PropertiesEntity properties, MainDeploymentBuildVersionText mainDeploymentBuildVersionText) {
    super(properties, mainDeploymentBuildVersionText);
  }
}
