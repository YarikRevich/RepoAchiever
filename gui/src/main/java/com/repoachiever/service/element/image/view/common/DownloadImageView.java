package com.repoachiever.service.element.image.view.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApplicationImageFileNotFoundException;
import com.repoachiever.service.element.scene.main.deployment.MainDeploymentScene;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.payload.DownloadEvent;
import ink.bluecloud.css.CssResources;
import ink.bluecloud.css.ElementButton;
import ink.bluecloud.css.ElementButtonKt;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import org.springframework.context.ApplicationEventPublisher;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents download image view.
 */
public class DownloadImageView implements IElement<BorderPane> {
    private final UUID id = UUID.randomUUID();

    public DownloadImageView(
            PropertiesEntity properties,
            ApplicationEventPublisher applicationEventPublisher,
            MainDeploymentScene deploymentScene,
            String location)
            throws ApplicationImageFileNotFoundException {
        Button button = new Button();

        ElementButtonKt.theme(button, ElementButton.greenButton);
        button.getStylesheets().add(CssResources.globalCssFile);
        button.getStylesheets().add(CssResources.buttonCssFile);
        button.getStylesheets().add(CssResources.textFieldCssFile);

        DirectoryChooser directoryChooser = new DirectoryChooser();

        button.setOnMouseClicked(
                event -> {
                    File destination = directoryChooser.showDialog(deploymentScene.getContent().getWindow());

                    if (Objects.nonNull(destination)) {
                        applicationEventPublisher.publishEvent(new DownloadEvent(location, destination));
                    }
                });

        InputStream imageSource =
                getClass().getClassLoader().getResourceAsStream(properties.getImageDownloadName());
        if (Objects.isNull(imageSource)) {
            throw new ApplicationImageFileNotFoundException();
        }

        ImageView imageView = new ImageView(new Image(imageSource));
        imageView.setFitHeight(properties.getImageBarHeight());
        imageView.setFitWidth(properties.getImageBarWidth());

        button.setGraphic(imageView);

        button.setAlignment(Pos.CENTER_RIGHT);

        SplitPane splitPane = new SplitPane(button);
        splitPane.setTooltip(new Tooltip(properties.getButtonDownloadDescription()));

        BorderPane borderPane = new BorderPane();
        borderPane.setRight(splitPane);

        ElementStorage.setElement(id, borderPane);
    }

    /**
     * @see IElementActualizable
     */
    @Override
    public BorderPane getContent() {
        return ElementStorage.getElement(id);
    }
}
