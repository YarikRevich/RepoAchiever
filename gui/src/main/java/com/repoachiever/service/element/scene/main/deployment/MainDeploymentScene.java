package com.repoachiever.service.element.scene.main.deployment;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.deployment.MainDeploymentSceneLayout;
import com.repoachiever.service.element.menu.TabMenuBar;
import com.repoachiever.service.element.progressbar.main.deployment.MainDeploymentCircleProgressBar;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainDeploymentScene implements IElement<Scene> {
  UUID id = UUID.randomUUID();

  public MainDeploymentScene(
      @Autowired PropertiesEntity properties,
      @Autowired MainDeploymentSceneLayout deploymentSceneLayout,
      @Autowired MainDeploymentCircleProgressBar mainDeploymentCircleProgressBar) {
    Group group = new Group();
    group.getChildren().add(deploymentSceneLayout.getContent());
    group.getChildren().add(mainDeploymentCircleProgressBar.getContent());

    ElementStorage.setElement(
        id,
        new Scene(
            group,
            Color.rgb(
                properties.getGeneralBackgroundColorR(),
                properties.getGeneralBackgroundColorG(),
                properties.getGeneralBackgroundColorB())));
  }

  @Override
  public Scene getContent() {
    return ElementStorage.getElement(id);
  }
}
