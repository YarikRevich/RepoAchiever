package com.repoachiever.service.element.text.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.font.FontLoader;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.event.state.LocalState;
import java.util.UUID;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents landing announcement text presented at start scene. */
@Service
public class LandingAnnouncementText implements IElementResizable, IElement<Label> {
  private final UUID id = UUID.randomUUID();

  public LandingAnnouncementText(@Autowired PropertiesEntity properties) {
    Label label = new Label(properties.getLabelWelcomeMessage());

    label.setFont(FontLoader.getFont20());
    label.setAlignment(Pos.TOP_CENTER);
    label.setTextAlignment(TextAlignment.CENTER);
    label.setWrapText(true);

    label.setStyle(
        String.format(
            "-fx-background-color: rgb(%d, %d, %d); " + "-fx-background-radius: 10;",
            properties.getCommonSceneContentBackgroundColorR(),
            properties.getCommonSceneContentBackgroundColorG(),
            properties.getCommonSceneContentBackgroundColorB()));

    ElementStorage.setElement(id, label);
    ElementStorage.setResizable(this);
  }

  /**
   * @see IElement
   */
  @Override
  public Label getContent() {
    return ElementStorage.getElement(id);
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefWidth() {
    getContent().setMaxWidth(LocalState.getMainWindowWidth());
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {
    getContent().setMaxHeight(LocalState.getMainWindowHeight());
  }
}
