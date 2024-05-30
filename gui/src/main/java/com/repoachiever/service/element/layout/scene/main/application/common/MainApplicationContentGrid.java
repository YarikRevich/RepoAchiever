package com.repoachiever.service.element.layout.scene.main.application.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.list.ListVisualizer;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.state.LocalState;
import java.util.UUID;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents main application content grid used for content visualization. */
@Service
public class MainApplicationContentGrid implements IElementResizable, IElement<GridPane> {
  private final UUID id = UUID.randomUUID();

  public MainApplicationContentGrid(
      @Autowired PropertiesEntity properties,
      @Autowired MainApplicationBarGrid mainApplicationBarGrid,
      @Autowired ListVisualizer listVisualizer) {
    GridPane grid = new GridPane();
    grid.setVgap(properties.getCommonSceneContentVerticalGap());

    ColumnConstraints column1 = new ColumnConstraints();
    column1.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints().add(column1);

    RowConstraints row1 = new RowConstraints();
    row1.setPercentHeight(9);
    RowConstraints row2 = new RowConstraints();
    row2.setPercentHeight(91);

    grid.getRowConstraints().addAll(row1, row2);

    grid.addColumn(0, mainApplicationBarGrid.getContent(), listVisualizer.getContent());

    ElementStorage.setElement(id, grid);
    ElementStorage.setResizable(this);
  }

  /**
   * @see IElement
   */
  @Override
  public GridPane getContent() {
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
