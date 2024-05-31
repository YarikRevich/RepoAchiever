package com.repoachiever.service.element.list.cell;

import com.repoachiever.dto.ListVisualizerCellInputDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.common.CleanImageView;
import com.repoachiever.service.element.image.view.common.DownloadImageView;
import com.repoachiever.service.element.scene.main.deployment.MainDeploymentScene;
import com.repoachiever.service.state.StateService;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Objects;

/**
 * Represents list visualizer cell entity, which extends basic cell details.
 */
public class ListVisualizerCell extends ListCell<ListVisualizerCellInputDto> {
    private HBox hbox;

    private Label label;

    private final Pane pane = new Pane();

    private CleanImageView cleanImageView;

    private DownloadImageView downloadImageView;

    private final PropertiesEntity properties;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final MainDeploymentScene deploymentScene;

    public ListVisualizerCell(
            PropertiesEntity properties,
            ApplicationEventPublisher applicationEventPublisher,
            MainDeploymentScene deploymentScene) {
        super();

        this.properties = properties;
        this.applicationEventPublisher = applicationEventPublisher;
        this.deploymentScene = deploymentScene;

        HBox.setHgrow(pane, Priority.ALWAYS);
    }

    /**
     * @see ListCell
     */
    @Override
    @SneakyThrows
    protected void updateItem(ListVisualizerCellInputDto item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        setGraphic(null);

        if (!empty) {
            if (Objects.isNull(StateService.getConfigLocation())) {
                if (Objects.isNull(label)) {
                    label = new Label(properties.getListViewNotOpenedName());
                }

                if (Objects.isNull(hbox)) {
                    hbox = new HBox();

                    hbox.getChildren().addAll(label);
                }
            } else if (item.getEmpty()) {
                if (Objects.isNull(label)) {
                    label = new Label(properties.getListViewEmptyName());
                }

                if (Objects.isNull(hbox)) {
                    hbox = new HBox();

                    hbox.getChildren().addAll(label);
                }

                if (!label.getText().equals(properties.getListViewEmptyName())) {
                    label.setText(properties.getListViewEmptyName());

                    hbox.getChildren().clear();

                    hbox.getChildren().addAll(label);
                }
            } else {
                if (Objects.isNull(cleanImageView)) {
                    this.cleanImageView = new CleanImageView(
                            properties, applicationEventPublisher, item.getName());
                }

                if (Objects.isNull(downloadImageView)) {
                    this.downloadImageView = new DownloadImageView(
                            properties, applicationEventPublisher, deploymentScene, item.getName());
                }

                if (Objects.isNull(label)) {
                    label = new Label(item.getName());
                }

                if (!label.getText().equals(item.getName())) {
                    label.setText(item.getName());

                    if (Objects.isNull(hbox)) {
                        hbox = new HBox();
                    }

                    hbox.getChildren().addAll(pane, cleanImageView.getContent(), downloadImageView.getContent());
                }
            }
        }

        HBox.setHgrow(pane, Priority.ALWAYS);

        setGraphic(hbox);
    }
}
