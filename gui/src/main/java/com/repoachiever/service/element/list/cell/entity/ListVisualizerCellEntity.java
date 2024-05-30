package com.repoachiever.service.element.list.cell.entity;

import com.repoachiever.dto.VisualizerCellDto;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * Represents list visualizer cell entity, which extends basic cell details.
 */
public class ListVisualizerCellEntity extends ListCell<VisualizerCellDto> {
    private final HBox hbox = new HBox();

    private final Label label = new Label("");

    Button button = new Button("Del");

    public ListVisualizerCellEntity() {
        super();

        System.out.println("VISUAL");

        Pane pane = new Pane();

        hbox.getChildren().addAll(label, pane, button);

        HBox.setHgrow(pane, Priority.ALWAYS);

        button.setOnAction(event -> getListView().getItems().remove(getItem()));
    }

    /**
     * @see ListCell
     */
    @Override
    protected void updateItem(VisualizerCellDto item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        System.out.println("IT WORKS");


        if (empty) {
            label.setText("EMPTY");
        }
//
//        if (item != null && !empty) {
//            label.setText(item.getName());
//
//            setGraphic(hbox);
//        }
    }
}
