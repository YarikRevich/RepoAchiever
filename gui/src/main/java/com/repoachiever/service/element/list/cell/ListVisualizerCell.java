package com.repoachiever.service.element.list.cell;

import com.repoachiever.service.element.list.cell.entity.ListVisualizerCellEntity;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;

import java.util.UUID;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

/**
 * Represents list visualizer cell, which resolves access for more information to get the details.
 */
public class ListVisualizerCell implements IElement<ListVisualizerCellEntity> {
    private final UUID id = UUID.randomUUID();

    public ListVisualizerCell(String name, Boolean additional) {
        Label label = new Label(name);

        ElementStorage.setElement(id, label);
    }

    /**
     * @see IElement
     */
    @Override
    public ListVisualizerCellEntity getContent() {
        return ElementStorage.getElement(id);
    }
}
