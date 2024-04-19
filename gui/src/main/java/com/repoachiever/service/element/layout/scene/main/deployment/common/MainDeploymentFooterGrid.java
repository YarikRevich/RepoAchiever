package com.repoachiever.service.element.layout.scene.main.deployment.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.common.MainFooterGrid;
import com.repoachiever.service.element.text.main.deployment.MainDeploymentBuildVersionText;
import org.springframework.stereotype.Service;

/**
 * @see MainFooterGrid
 */
@Service
public class MainDeploymentFooterGrid extends MainFooterGrid {
  public MainDeploymentFooterGrid(
      PropertiesEntity properties, MainDeploymentBuildVersionText mainDeploymentBuildVersionText) {
    super(properties, mainDeploymentBuildVersionText);
  }
}
