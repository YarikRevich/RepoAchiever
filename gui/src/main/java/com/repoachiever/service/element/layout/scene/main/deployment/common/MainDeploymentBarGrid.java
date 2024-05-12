package com.repoachiever.service.element.layout.scene.main.deployment.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.common.EditDeploymentConfigurationImageView;
import com.repoachiever.service.element.image.view.common.RefreshDeploymentStateImageView;
import com.repoachiever.service.element.image.view.common.StartDeploymentImageView;
import com.repoachiever.service.element.image.view.common.StopDeploymentImageView;
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

@Service
public class MainDeploymentBarGrid implements IElementResizable, IElement<GridPane> {
  UUID id = UUID.randomUUID();

  public MainDeploymentBarGrid(
      @Autowired PropertiesEntity properties,
      @Autowired StartDeploymentImageView startDeploymentImageView,
      @Autowired StopDeploymentImageView stopDeploymentImageView,
      @Autowired RefreshDeploymentStateImageView refreshDeploymentStateImageView,
      @Autowired EditDeploymentConfigurationImageView editDeploymentConfigurationImageView) {
    GridPane grid = new GridPane();
    grid.setHgap(properties.getSceneCommonContentBarHorizontalGap());

    RowConstraints row1 = new RowConstraints();
    row1.setVgrow(Priority.ALWAYS);

    grid.getRowConstraints().add(row1);

    ColumnConstraints column1 = new ColumnConstraints();
    column1.setHgrow(Priority.ALWAYS);
    column1.setPercentWidth(10);

    ColumnConstraints column2 = new ColumnConstraints();
    column2.setHgrow(Priority.ALWAYS);
    column2.setPercentWidth(10);

    ColumnConstraints column3 = new ColumnConstraints();
    column3.setHgrow(Priority.ALWAYS);
    column3.setPercentWidth(10);

    ColumnConstraints column4 = new ColumnConstraints();
    column4.setHgrow(Priority.ALWAYS);
    column4.setPercentWidth(10);

    ColumnConstraints column5 = new ColumnConstraints();
    column5.setHgrow(Priority.ALWAYS);
    column5.setPercentWidth(60);

    grid.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

    grid.addRow(
        0,
        startDeploymentImageView.getContent(),
        stopDeploymentImageView.getContent(),
        refreshDeploymentStateImageView.getContent(),
        editDeploymentConfigurationImageView.getContent());

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
