package com.repoachiever.service.element.image.view.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.image.collection.ConnectionStatusImageCollection;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.state.StateService;
import java.util.Objects;
import java.util.UUID;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents connection status image view. */
@Service
public class ConnectionStatusImageView
    implements IElementActualizable, IElementResizable, IElement<BorderPane> {
  private final UUID id = UUID.randomUUID();

  private final PropertiesEntity properties;

  public ConnectionStatusImageView(@Autowired PropertiesEntity properties) {
    Button button = new Button();
    button.setDisable(true);
    button.setAlignment(Pos.CENTER_RIGHT);

    SplitPane splitPane = new SplitPane(button);
    splitPane.setTooltip(new Tooltip(properties.getLabelConnectionStatusFailureDescription()));

    splitPane.setBackground(
        Background.fill(
            Color.rgb(
                properties.getCommonSceneHeaderConnectionStatusBackgroundColorR(),
                properties.getCommonSceneHeaderConnectionStatusBackgroundColorG(),
                properties.getCommonSceneHeaderConnectionStatusBackgroundColorB())));

    BorderPane borderPane = new BorderPane();
    borderPane.setRight(splitPane);

    ElementStorage.setElement(id, borderPane);
    ElementStorage.setActualizable(this);
    ElementStorage.setResizable(this);

    this.properties = properties;
  }

  /**
   * @see IElementActualizable
   */
  @Override
  public BorderPane getContent() {
    return ElementStorage.getElement(id);
  }

  /**
   * @see IElementActualizable
   */
  @Override
  public void handleBackgroundUpdates() {
    Platform.runLater(
        () -> {
          BorderPane borderPane = getContent();

          SplitPane splitPane = (SplitPane) borderPane.getRight();
          Button button = (Button) splitPane.getItems().get(0);

          if (StateService.getConnectionEstablished()) {
            splitPane.getTooltip().setText(properties.getLabelConnectionStatusSuccessDescription());
            button.setGraphic(
                ConnectionStatusImageCollection.getSuccessfulConnectionStatusImage(
                    ElementHelper.getCircularElementSize(
                        StateService.getMainWindowWidth(), properties.getStatusImageScale())));

          } else {
            splitPane.getTooltip().setText(properties.getLabelConnectionStatusFailureDescription());
            button.setGraphic(
                ConnectionStatusImageCollection.getFailedConnectionStatusImage(
                    ElementHelper.getCircularElementSize(
                        StateService.getMainWindowWidth(), properties.getStatusImageScale())));

          }
        });
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefWidth() {
    Platform.runLater(
        () -> {
          BorderPane borderPane = getContent();
          SplitPane splitPane = (SplitPane) borderPane.getRight();
          Button button = (Button) splitPane.getItems().get(0);

          if (Objects.isNull(button.getGraphic())) {
            button.setGraphic(
                ConnectionStatusImageCollection.getFailedConnectionStatusImage(
                    ElementHelper.getCircularElementSize(
                        StateService.getMainWindowWidth(), properties.getStatusImageScale())));
          }
        });
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {}
}
