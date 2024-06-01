package com.repoachiever.service.element.layout.common;

import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import java.util.UUID;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;

/** Represents content grid used in main stage. */
public class ContentGrid implements IElement<GridPane> {
  private final UUID id = UUID.randomUUID();

  public ContentGrid(VBox buttons, Node element) {
    GridPane grid = new GridPane();
    grid.setPadding(new Insets(5, 5, 5, 5));
    grid.setHgap(10);

    RowConstraints row1 = new RowConstraints();
    row1.setVgrow(Priority.ALWAYS);

    grid.getRowConstraints().add(0, row1);

    ColumnConstraints column1 = new ColumnConstraints();
    column1.setHgrow(Priority.ALWAYS);
    column1.setPercentWidth(30);

    ColumnConstraints column2 = new ColumnConstraints();
    column2.setHgrow(Priority.ALWAYS);
    column2.setPercentWidth(70);

    grid.getColumnConstraints().addAll(column1, column2);

    grid.addColumn(0, buttons);
    grid.addColumn(1, element);

    ElementStorage.setElement(id, grid);
  }

  /**
   * @see IElement
   */
  @Override
  public GridPane getContent() {
    return ElementStorage.getElement(id);
  }
}
