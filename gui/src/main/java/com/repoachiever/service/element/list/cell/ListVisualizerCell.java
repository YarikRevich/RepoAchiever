package com.repoachiever.service.element.list.cell;

import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import java.util.UUID;
import javafx.scene.control.Label;

/**
 * Represents list visualizer cell, which resolves access for more information to get the details.
 */
public class ListVisualizerCell implements IElement<Label> {
  UUID id = UUID.randomUUID();

  public ListVisualizerCell(String text) {
    Label label = new Label(text);

    ElementStorage.setElement(id, label);
  }

  /**
   * @see IElement
   */
  @Override
  public Label getContent() {
    return ElementStorage.getElement(id);
  }
}
