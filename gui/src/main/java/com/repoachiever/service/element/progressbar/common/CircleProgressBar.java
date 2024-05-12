package com.repoachiever.service.element.progressbar.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.common.ElementHelper;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.state.LocalState;
import java.util.UUID;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents circular progress bar indicator. */
@Service
public class CircleProgressBar implements IElementResizable, IElement<VBox> {
  UUID id = UUID.randomUUID();

  public CircleProgressBar(@Autowired PropertiesEntity properties) {
    ProgressIndicator progressBar = new ProgressIndicator();
    progressBar.setStyle("-fx-progress-color: red;");
    progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

    VBox vbox = new VBox(progressBar);
    vbox.setAlignment(Pos.CENTER);
    vbox.setVisible(false);

    ElementStorage.setElement(id, vbox);
    ElementStorage.setResizable(this);
  }

  /**
   * @see IElement
   */
  @Override
  public VBox getContent() {
    return ElementStorage.getElement(id);
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefWidth() {
    Point2D centralPoint =
        ElementHelper.getCentralPoint(
            LocalState.getMainWindowWidth(), LocalState.getMainWindowHeight());

    getContent().setTranslateX(centralPoint.getX() - 30);
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {
    Point2D centralPoint =
        ElementHelper.getCentralPoint(
            LocalState.getMainWindowWidth(), LocalState.getMainWindowHeight());

    getContent().setTranslateY(centralPoint.getY() - 30);
  }
}
