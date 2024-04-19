package com.repoachiever.service.element.text.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.font.FontLoader;
import com.repoachiever.service.element.storage.ElementStorage;
import java.util.UUID;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildVersionText implements IElementResizable, IElement<Label> {
  UUID id = UUID.randomUUID();

  public BuildVersionText(@Autowired PropertiesEntity properties) {
    Label label = new Label(String.format("BuildVersion: %s", properties.getGitCommitId()));
    label.setFont(FontLoader.getFont12());
    label.setAlignment(Pos.CENTER_LEFT);
    label.setWrapText(true);
    label.setPadding(new Insets(0, 0, 0, 10));

    ElementStorage.setElement(id, label);
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
  public void handlePrefWidth() {}

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {}
}
