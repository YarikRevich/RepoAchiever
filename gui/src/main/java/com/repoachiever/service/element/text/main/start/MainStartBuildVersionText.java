package com.repoachiever.service.element.text.main.start;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.text.common.BuildVersionText;
import org.springframework.stereotype.Service;

/**
 * @see BuildVersionText
 */
@Service
public class MainStartBuildVersionText extends BuildVersionText {
  public MainStartBuildVersionText(PropertiesEntity properties) {
    super(properties);
  }
}
