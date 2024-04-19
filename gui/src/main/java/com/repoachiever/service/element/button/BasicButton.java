package com.repoachiever.service.element.button;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.state.LocalState;
import ink.bluecloud.css.CssResources;
import ink.bluecloud.css.ElementButton;
import ink.bluecloud.css.ElementButtonKt;
import java.util.UUID;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;

/** Represents basic button. */
public class BasicButton implements IElementResizable, IElement<Button> {
  UUID id = UUID.randomUUID();

  private final PropertiesEntity properties;

  public BasicButton(String text, PropertiesEntity properties, Runnable action) {
    this.properties = properties;

    Button basicButton = new Button();
    ElementButtonKt.theme(basicButton, ElementButton.redButton);
    basicButton.getStylesheets().add(CssResources.globalCssFile);
    basicButton.getStylesheets().add(CssResources.buttonCssFile);
    basicButton.getStylesheets().add(CssResources.textFieldCssFile);

    basicButton.setText(text);
    basicButton.setOnAction(event -> action.run());

    ElementStorage.setElement(id, basicButton);
    ElementStorage.setResizable(this);
  }

  /**
   * @see IElement
   */
  @Override
  public Button getContent() {
    return ElementStorage.getElement(id);
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefWidth() {
    Rectangle2D size =
        ElementHelper.getSizeWithScale(
            LocalState.getMainWindowWidth(),
            LocalState.getMainWindowHeight(),
            properties.getBasicButtonSizeWidth(),
            properties.getBasicButtonSizeHeight());

    getContent().setPrefWidth(size.getWidth());
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {
    Rectangle2D size =
        ElementHelper.getSizeWithScale(
            LocalState.getMainWindowWidth(),
            LocalState.getMainWindowHeight(),
            properties.getBasicButtonSizeWidth(),
            properties.getBasicButtonSizeHeight());

    getContent().setPrefHeight(size.getHeight());
  }
}
