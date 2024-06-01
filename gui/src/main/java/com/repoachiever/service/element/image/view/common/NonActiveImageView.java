package com.repoachiever.service.element.image.view.common;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApplicationImageFileNotFoundException;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents non active image view.
 */
public class NonActiveImageView implements IElement<BorderPane> {
    private final UUID id = UUID.randomUUID();

    public NonActiveImageView(PropertiesEntity properties) throws ApplicationImageFileNotFoundException {
        Button button = new Button();

        button.setDisable(true);

        InputStream imageSource =
                getClass().getClassLoader().getResourceAsStream(properties.getImageNonActiveName());
        if (Objects.isNull(imageSource)) {
            throw new ApplicationImageFileNotFoundException();
        }

        ImageView imageView = new ImageView(new Image(imageSource));
        imageView.setFitHeight(properties.getImageBarHeight());
        imageView.setFitWidth(properties.getImageBarWidth());

        button.setGraphic(imageView);

        button.setAlignment(Pos.CENTER_RIGHT);

        SplitPane splitPane = new SplitPane(button);
        splitPane.setTooltip(new Tooltip(properties.getLabelNonActiveDescription()));

        splitPane.setBackground(
                Background.fill(
                        Color.rgb(
                                properties.getCommonSceneHeaderConnectionStatusBackgroundColorR(),
                                properties.getCommonSceneHeaderConnectionStatusBackgroundColorG(),
                                properties.getCommonSceneHeaderConnectionStatusBackgroundColorB())));

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
