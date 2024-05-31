package com.repoachiever.service.element.image.view.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApplicationImageFileNotFoundException;
import com.repoachiever.service.element.scene.main.deployment.MainDeploymentScene;
import com.repoachiever.service.element.scene.main.start.MainStartScene;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.payload.OpenEvent;
import ink.bluecloud.css.CssResources;
import ink.bluecloud.css.ElementButton;
import ink.bluecloud.css.ElementButtonKt;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/** Represents open image view. */
@Service
public class OpenImageView implements IElementResizable, IElement<BorderPane> {
  private final UUID id = UUID.randomUUID();

  @Lazy
  @Autowired private MainDeploymentScene deploymentScene;

  public OpenImageView(
      @Autowired PropertiesEntity properties,
      @Autowired ApplicationEventPublisher applicationEventPublisher)
      throws ApplicationImageFileNotFoundException {
    Button button = new Button();

    ElementButtonKt.theme(button, ElementButton.greenButton);
    button.getStylesheets().add(CssResources.globalCssFile);
    button.getStylesheets().add(CssResources.buttonCssFile);
    button.getStylesheets().add(CssResources.textFieldCssFile);

    FileChooser fileChooser = new FileChooser();

    button.setOnMouseClicked(event -> {
      File file = fileChooser.showOpenDialog(deploymentScene.getContent().getWindow());

      if (Objects.nonNull(file)) {
        applicationEventPublisher.publishEvent(new OpenEvent(file));
      }
    });

    InputStream imageSource =
        getClass().getClassLoader().getResourceAsStream(properties.getImageOpenName());
    if (Objects.isNull(imageSource)) {
      throw new ApplicationImageFileNotFoundException();
    }

    ImageView imageView = new ImageView(new Image(imageSource));
    imageView.setFitHeight(properties.getImageBarHeight());
    imageView.setFitWidth(properties.getImageBarWidth());

    button.setGraphic(imageView);

    button.setAlignment(Pos.CENTER_RIGHT);

    SplitPane splitPane = new SplitPane(button);
    splitPane.setTooltip(new Tooltip(properties.getButtonOpenDescription()));

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
