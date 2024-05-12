package com.repoachiever.service.element.text.main.deployment;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.text.common.BuildVersionText;
import org.springframework.stereotype.Service;

/**
 * @see BuildVersionText
 */
@Service
public class MainDeploymentBuildVersionText extends BuildVersionText {
  public MainDeploymentBuildVersionText(PropertiesEntity properties) {
    super(properties);
  }
}
