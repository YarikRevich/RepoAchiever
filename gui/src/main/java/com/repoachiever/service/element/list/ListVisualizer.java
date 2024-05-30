package com.repoachiever.service.element.list;

import com.repoachiever.dto.VisualizerCellDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.ContentUnit;
import com.repoachiever.service.element.list.cell.ListVisualizerCell;
import com.repoachiever.service.element.list.cell.entity.ListVisualizerCellEntity;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.state.LocalState;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Represents list visualizer for deployment data.
 */
@Service
public class ListVisualizer
        implements IElementResizable, IElementActualizable, IElement<ListView<VisualizerCellDto>> {
    private final UUID id = UUID.randomUUID();

    @Autowired
    private PropertiesEntity properties;

    public ListVisualizer(@Autowired ApplicationEventPublisher applicationEventPublisher) {
        List<VisualizerCellDto> items = List.of(VisualizerCellDto.of("d", false));

        ObservableList<VisualizerCellDto> myObservableList =
                FXCollections.observableList(items);

        ListView<VisualizerCellDto> listView = new ListView<>(myObservableList);

        listView.setCellFactory(element -> new ListVisualizerCellEntity());

//        listView.setOnMouseClicked(
//                event -> {
////          applicationEventPublisher.publishEvent(
////              new SwapFileOpenWindowEvent(
////                  LocalState.getDeploymentState().getResult().stream()
////                      .filter(
////                          element ->
////                              element
////                                  .getName()
////                                  .equals(listView.getSelectionModel().getSelectedItem()))
////                      .toList()));
//                });

        ElementStorage.setElement(id, listView);
        ElementStorage.setActualizable(this);
        ElementStorage.setResizable(this);
    }

    /**
     * @see IElement
     */
    @Override
    public ListView<VisualizerCellDto> getContent() {
        return ElementStorage.getElement(id);
    }

    /**
     * @see IElementResizable
     */
    @Override
    public void handlePrefWidth() {
        getContent().setMaxWidth(LocalState.getMainWindowWidth());
    }

    /**
     * @see IElementResizable
     */
    @Override
    public void handlePrefHeight() {
        getContent().setMaxHeight(LocalState.getMainWindowHeight());
    }

    /**
     * @see IElementActualizable
     */
    @Override
    public void handleBackgroundUpdates() {
        ContentUnit content = LocalState.getContent();

        Platform.runLater(
                () -> {
                    if (!Objects.isNull(content)) {
                        Set<VisualizerCellDto> items =
                                content
                                        .getLocations()
                                        .stream()
                                        .map(element ->
                                                VisualizerCellDto.of(element.getName(), element.getAdditional()))
                                        .collect(Collectors.toSet());

                        ObservableList<VisualizerCellDto> myObservableList =
                                FXCollections.observableList(items.stream().toList());

                        getContent().setItems(myObservableList);
                        getContent().setCellFactory(element -> new ListVisualizerCellEntity());

                        getContent().setMouseTransparent(false);
                        getContent().setFocusTraversable(true);
                    } else {
                        System.out.println("IN HERE");

                        List<VisualizerCellDto> items = new ArrayList<>();

                        ObservableList<VisualizerCellDto> myObservableList =
                                FXCollections.observableList(items);

                        getContent().setCellFactory(element -> new ListVisualizerCellEntity());
                        getContent().setItems(myObservableList);

                        getContent().setMouseTransparent(true);
                        getContent().setFocusTraversable(false);
                    }
                });
    }
}
