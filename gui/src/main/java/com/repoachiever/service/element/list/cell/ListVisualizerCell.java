package com.repoachiever.service.element.list.cell;

import com.repoachiever.dto.ListVisualizerCellInputDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.image.view.common.ActiveImageView;
import com.repoachiever.service.element.image.view.common.CleanImageView;
import com.repoachiever.service.element.image.view.common.DownloadImageView;
import com.repoachiever.service.element.image.view.common.NonActiveImageView;
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

    private ActiveImageView activeImageView;

    private NonActiveImageView nonActiveImageView;

    private final Pane pane = new Pane();

    private CleanImageView cleanImageView;

    private DownloadImageView downloadImageView;

    private final PropertiesEntity properties;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final MainDeploymentScene deploymentScene;

    private Boolean prevActive = false;

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

        if (!empty) {
            if (Objects.isNull(StateService.getConfigLocation())) {
                if (Objects.isNull(label)) {
                    label = new Label(properties.getListViewNotOpenedName());
                }

                if (Objects.isNull(hbox)) {
                    hbox = new HBox();

                    hbox.setSpacing(properties.getSceneCommonContentBarHorizontalGap());

                    hbox.getChildren().addAll(label);
                }
            } else if (item.getStub()) {
                if (Objects.isNull(label)) {
                    label = new Label(properties.getListViewEmptyName());
                }

                if (Objects.isNull(hbox)) {
                    hbox = new HBox();

                    hbox.setSpacing(properties.getSceneCommonContentBarHorizontalGap());

                    hbox.getChildren().addAll(label);
                }

                if (!label.getText().equals(properties.getListViewEmptyName())) {
                    label.setText(properties.getListViewEmptyName());

                    hbox.getChildren().clear();

                    hbox.getChildren().addAll(label);
                }
            } else if (item.getEmpty()) {
                if (Objects.isNull(label)) {
                    label = new Label(item.getName());
                }

                if (!label.getText().equals(item.getName())) {
                    label.setText(item.getName());
                }

                if (item.getActive()) {
                    if (Objects.isNull(activeImageView)) {
                        this.activeImageView = new ActiveImageView(properties);
                    }
                } else {
                    if (Objects.isNull(nonActiveImageView)) {
                        this.nonActiveImageView = new NonActiveImageView(properties);
                    }
                }

                if (Objects.isNull(hbox)) {
                    hbox = new HBox();

                    hbox.setSpacing(properties.getSceneCommonContentBarHorizontalGap());

                    if (item.getActive()) {
                        hbox.getChildren().addAll(label, activeImageView.getContent());
                    } else {
                        hbox.getChildren().addAll(label, nonActiveImageView.getContent());
                    }
                }

                if (hbox.getChildren().size() > 2 || hbox.getChildren().size() == 1) {
                    hbox.getChildren().clear();

                    if (item.getActive()) {
                        hbox.getChildren().addAll(label, activeImageView.getContent());
                    } else {
                        hbox.getChildren().addAll(label, nonActiveImageView.getContent());
                    }
                }
            } else {
                if (Objects.isNull(label)) {
                    label = new Label(item.getName());
                }

                if (!label.getText().equals(item.getName())) {
                    label.setText(item.getName());
                }

                if (Objects.isNull(hbox)) {
                    hbox = new HBox();

                    hbox.setSpacing(properties.getSceneCommonContentBarHorizontalGap());
                }

                if (Objects.isNull(cleanImageView)) {
                    this.cleanImageView = new CleanImageView(properties, applicationEventPublisher, item.getName());
                }

                if (Objects.isNull(downloadImageView)) {
                    this.downloadImageView = new DownloadImageView(
                            properties, applicationEventPublisher, deploymentScene, item.getName());
                }

                if (item.getActive()) {
                    if (Objects.isNull(activeImageView)) {
                        this.activeImageView = new ActiveImageView(properties);
                    }
                } else {
                    if (Objects.isNull(nonActiveImageView)) {
                        this.nonActiveImageView = new NonActiveImageView(properties);
                    }
                }

                if (hbox.getChildren().size() < 5 || prevActive != item.getActive()) {
                    hbox.getChildren().clear();

                    if (item.getActive()) {
                        hbox.getChildren().addAll(
                                label,
                                activeImageView.getContent(),
                                pane,
                                cleanImageView.getContent(),
                                downloadImageView.getContent());
                    } else {
                        hbox.getChildren().addAll(
                                label,
                                nonActiveImageView.getContent(),
                                pane,
                                cleanImageView.getContent(),
                                downloadImageView.getContent());
                    }

                    prevActive = item.getActive();
                }
            }

            HBox.setHgrow(pane, Priority.ALWAYS);

            setGraphic(hbox);
        } else {
            setText(null);

            setGraphic(null);
        }
    }
}
