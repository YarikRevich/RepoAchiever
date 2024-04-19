package com.repoachiever.service.element.layout.scene.main.start;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.layout.common.ContentGrid;
import com.repoachiever.service.element.layout.scene.main.start.common.MainStartFooterGrid;
import com.repoachiever.service.element.layout.scene.main.start.common.MainStartHeaderGrid;
import com.repoachiever.service.element.layout.scene.main.start.common.MainStartMenuButtonBox;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.element.text.common.LandingAnnouncementText;
import com.repoachiever.service.event.state.LocalState;
import java.util.UUID;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start scene layout of the main stage. */
@Service
public class MainStartSceneLayout implements IElementResizable, IElement<GridPane> {
  UUID id = UUID.randomUUID();

  public MainStartSceneLayout(
      @Autowired PropertiesEntity properties,
      @Autowired MainStartMenuButtonBox mainStartMenuButtonBox,
      @Autowired LandingAnnouncementText landingAnnouncementText,
      @Autowired MainStartHeaderGrid mainStartHeaderGrid,
      @Autowired MainStartFooterGrid mainStartFooterGrid) {
    GridPane grid = new GridPane();

    ColumnConstraints column1 = new ColumnConstraints();
    column1.setHgrow(Priority.ALWAYS);

    grid.getColumnConstraints().add(column1);

    RowConstraints row1 = new RowConstraints();
    row1.setPercentHeight(5);
    RowConstraints row2 = new RowConstraints();
    row2.setPercentHeight(86);
    RowConstraints row3 = new RowConstraints();
    row3.setPercentHeight(4);

    grid.getRowConstraints().addAll(row1, row2, row3);

    ContentGrid contentGrid =
        new ContentGrid(mainStartMenuButtonBox.getContent(), landingAnnouncementText.getContent());

    grid.addColumn(
        0,
        mainStartHeaderGrid.getContent(),
        contentGrid.getContent(),
        mainStartFooterGrid.getContent());

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
