package com.repoachiever.service.element.scene.main.start;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.scene.main.start.MainStartSceneLayout;
import com.repoachiever.service.element.menu.TabMenuBar;
import com.repoachiever.service.element.progressbar.main.start.MainStartCircleProgressBar;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import java.util.UUID;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** */
@Service
public class MainStartScene implements IElement<Scene> {
  private final UUID id = UUID.randomUUID();

  public MainStartScene(
      @Autowired PropertiesEntity properties,
      @Autowired MainStartSceneLayout startSceneLayout,
      @Autowired MainStartCircleProgressBar mainStartCircleProgressBar) {
    Group group = new Group();
    group.getChildren().add(startSceneLayout.getContent());
    group.getChildren().add(mainStartCircleProgressBar.getContent());

    ElementStorage.setElement(
        id,
        new Scene(
            group,
            Color.rgb(
                properties.getGeneralBackgroundColorR(),
                properties.getGeneralBackgroundColorG(),
                properties.getGeneralBackgroundColorB())));
  }

  public Scene getContent() {
    return ElementStorage.getElement(id);
  }
}
