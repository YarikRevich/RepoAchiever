package com.repoachiever.service.element.image.view.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApplicationImageFileNotFoundException;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.payload.DeploymentStateRetrievalEvent;
import ink.bluecloud.css.CssResources;
import ink.bluecloud.css.ElementButton;
import ink.bluecloud.css.ElementButtonKt;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** Represents deployment refresh image. */
@Service
public class RefreshDeploymentStateImageView implements IElementResizable, IElement<BorderPane> {
  UUID id = UUID.randomUUID();

  private static final String REFRESH_DEPLOYMENT_DESCRIPTION = "refresh deployment state";

  public RefreshDeploymentStateImageView(
      @Autowired PropertiesEntity properties,
      @Autowired ApplicationEventPublisher applicationEventPublisher)
      throws ApplicationImageFileNotFoundException {
    Button button = new Button();
    ElementButtonKt.theme(button, ElementButton.redButton);
    button.getStylesheets().add(CssResources.globalCssFile);
    button.getStylesheets().add(CssResources.buttonCssFile);
    button.getStylesheets().add(CssResources.textFieldCssFile);

    button.setOnMouseClicked(
        event -> applicationEventPublisher.publishEvent(new DeploymentStateRetrievalEvent()));

    InputStream imageSource =
        getClass().getClassLoader().getResourceAsStream(properties.getImageRefreshName());
    if (Objects.isNull(imageSource)) {
      throw new ApplicationImageFileNotFoundException();
    }

    ImageView imageView = new ImageView(new Image(imageSource));
    imageView.setFitHeight(properties.getImageBarHeight());
    imageView.setFitWidth(properties.getImageBarWidth());

    button.setGraphic(imageView);

    button.setAlignment(Pos.CENTER_RIGHT);

    SplitPane splitPane = new SplitPane(button);
    splitPane.setTooltip(new Tooltip(REFRESH_DEPLOYMENT_DESCRIPTION));

    BorderPane borderPane = new BorderPane();
    borderPane.setRight(splitPane);

    ElementStorage.setElement(id, borderPane);
    ElementStorage.setResizable(this);
  }

  /**
   * @see IElementActualizable
   */
  @Override
  public BorderPane getContent() {
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
