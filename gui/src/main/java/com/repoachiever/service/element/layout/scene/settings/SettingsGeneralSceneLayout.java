package com.repoachiever.service.element.layout.scene.settings;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.common.ContentGrid;
import com.repoachiever.service.element.layout.scene.settings.common.SettingsMenuButtonBox;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.element.text.common.LandingAnnouncementText;
import com.repoachiever.service.event.state.LocalState;
import java.util.UUID;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/** Represents general settings layout of the settings stage. */
@Service
public class SettingsGeneralSceneLayout implements IElementResizable, IElement<GridPane> {
  private final UUID id = UUID.randomUUID();

  public SettingsGeneralSceneLayout(
      @Autowired PropertiesEntity properties,
      @Autowired SettingsMenuButtonBox settingsMenuButtonBox,
      @Autowired LandingAnnouncementText landingAnnouncementText) {
    GridPane grid = new GridPane();

    ColumnConstraints column1 = new ColumnConstraints();
    column1.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints().add(column1);

    ContentGrid contentGrid =
        new ContentGrid(settingsMenuButtonBox.getContent(), landingAnnouncementText.getContent());

    RowConstraints row1 = new RowConstraints();
    row1.setPercentHeight(95);

    grid.getRowConstraints().addAll(row1);

    grid.addColumn(0, contentGrid.getContent());

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
    getContent().setMinWidth(LocalState.getMainWindowWidth());
  }

  /**
   * @see IElementResizable
   */
  @Override
  public void handlePrefHeight() {
    getContent().setMinHeight(LocalState.getMainWindowHeight());
  }
}
