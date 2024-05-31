package com.repoachiever.service.element.list;

import com.repoachiever.dto.ListVisualizerCellInputDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.service.element.list.cell.ListVisualizerCell;
import com.repoachiever.service.element.scene.main.deployment.MainDeploymentScene;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.state.StateService;

import java.util.*;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Represents list visualizer for deployment data.
 */
@Service
public class ListVisualizer
        implements IElementResizable, IElementActualizable, IElement<ListView<ListVisualizerCellInputDto>> {
    private final UUID id = UUID.randomUUID();

    @Lazy
    @Autowired private MainDeploymentScene deploymentScene;

    public ListVisualizer(
            @Autowired PropertiesEntity properties,
            @Autowired ApplicationEventPublisher applicationEventPublisher,
            @Lazy @Autowired MainDeploymentScene deploymentScene) {
        ListView<ListVisualizerCellInputDto> listView = new ListView<>();

        listView.setCellFactory(element ->
                new ListVisualizerCell(properties, applicationEventPublisher, deploymentScene));

        listView.setOnMouseClicked(
                event -> {
                    if (Objects.nonNull(listView.getSelectionModel().getSelectedItem())) {
                        System.out.println(listView.getSelectionModel().getSelectedItem().getName());
                    }
//          applicationEventPublisher.publishEvent(
//              new SwapFileOpenWindowEvent(
//                  LocalState.getDeploymentState().getResult().stream()
//                      .filter(
//                          element ->
//                              element
//                                  .getName()
//                                  .equals(listView.getSelectionModel().getSelectedItem()))
//                      .toList()));
                });

        ElementStorage.setElement(id, listView);
        ElementStorage.setActualizable(this);
        ElementStorage.setResizable(this);
    }

    /**
     * @see IElement
     */
    @Override
    public ListView<ListVisualizerCellInputDto> getContent() {
        return ElementStorage.getElement(id);
    }

    /**
     * @see IElementResizable
     */
    @Override
    public void handlePrefWidth() {
        getContent().setMaxWidth(StateService.getMainWindowWidth());
    }

    /**
     * @see IElementResizable
     */
    @Override
    public void handlePrefHeight() {
        getContent().setMaxHeight(StateService.getMainWindowHeight());
    }

    /**
     * @see IElementActualizable
     */
    @Override
    public void handleBackgroundUpdates() {
        ContentRetrievalResult content = StateService.getContent();

        Platform.runLater(
                () -> {
                    ObservableList<ListVisualizerCellInputDto> observableList;

                    if (!Objects.isNull(content)) {
                        List<ListVisualizerCellInputDto> items =
                                content
                                        .getLocations()
                                        .stream()
                                        .map(element ->
                                                ListVisualizerCellInputDto.of(element.getName(), element.getActive()))
                                        .toList();

                        observableList =
                                FXCollections.observableList(items);

                        getContent().setMouseTransparent(false);
                        getContent().setFocusTraversable(true);
                    } else {
                        List<ListVisualizerCellInputDto> items =
                                Stream.of(ListVisualizerCellInputDto.empty())
                                        .toList();

                        observableList =
                                FXCollections.observableList(items);

                        getContent().setMouseTransparent(true);
                        getContent().setFocusTraversable(false);
                    }

                    getContent().setItems(observableList);
                });
    }
}
