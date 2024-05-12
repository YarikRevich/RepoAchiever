package com.repoachiever.service.element.layout.scene.main.deployment;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.common.ContentGrid;
import com.repoachiever.service.element.layout.scene.main.deployment.common.MainDeploymentContentGrid;
import com.repoachiever.service.element.layout.scene.main.deployment.common.MainDeploymentFooterGrid;
import com.repoachiever.service.element.layout.scene.main.deployment.common.MainDeploymentHeaderGrid;
import com.repoachiever.service.element.layout.scene.main.deployment.common.MainDeploymentMenuButtonBox;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.state.LocalState;
import java.util.UUID;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents deployment scene layout of the main stage. */
@Service
public class MainDeploymentSceneLayout implements IElementResizable, IElement<GridPane> {
  UUID id = UUID.randomUUID();

  public MainDeploymentSceneLayout(
      @Autowired PropertiesEntity properties,
      @Autowired MainDeploymentMenuButtonBox mainDeploymentMenuButtonBox,
      @Autowired MainDeploymentContentGrid mainDeploymentContentGrid,
      @Autowired MainDeploymentHeaderGrid mainDeploymentHeaderGrid,
      @Autowired MainDeploymentFooterGrid mainDeploymentFooterGrid) {
    GridPane grid = new GridPane();

    ColumnConstraints column1 = new ColumnConstraints();
    column1.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints().add(column1);

    RowConstraints row1 = new RowConstraints();
    row1.setPercentHeight(5);
    RowConstraints row2 = new RowConstraints();
    row2.setPercentHeight(85);
    RowConstraints row3 = new RowConstraints();
    row3.setPercentHeight(4);

    grid.getRowConstraints().addAll(row1, row2, row3);

    ContentGrid contentGrid =
        new ContentGrid(
            mainDeploymentMenuButtonBox.getContent(), mainDeploymentContentGrid.getContent());

    grid.addColumn(
        0,
        mainDeploymentHeaderGrid.getContent(),
        contentGrid.getContent(),
        mainDeploymentFooterGrid.getContent());

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
    getContent().setPrefWidth(LocalState.getMainWindowWidth());
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {
    getContent().setPrefHeight(LocalState.getMainWindowHeight());
  }
}
