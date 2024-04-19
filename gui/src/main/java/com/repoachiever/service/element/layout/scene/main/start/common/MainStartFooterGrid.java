package com.repoachiever.service.element.layout.scene.main.start.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.common.MainFooterGrid;
import com.repoachiever.service.element.text.main.start.MainStartBuildVersionText;
import org.springframework.stereotype.Service;

@Service
public class MainStartFooterGrid extends MainFooterGrid {
  public MainStartFooterGrid(
      PropertiesEntity properties, MainStartBuildVersionText mainStartBuildVersionText) {
    super(properties, mainStartBuildVersionText);
  }
}
