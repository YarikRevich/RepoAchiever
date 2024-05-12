package com.repoachiever.service.element.list;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.TopicLogsResult;
import com.repoachiever.service.element.list.cell.ListVisualizerCell;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.payload.SwapFileOpenWindowEvent;
import com.repoachiever.service.event.state.LocalState;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/** Represents list visualizer for deployment data. */
@Service
public class ListVisualizer
    implements IElementResizable, IElementActualizable, IElement<ListView<String>> {
  UUID id = UUID.randomUUID();

  @Autowired private PropertiesEntity properties;

  public ListVisualizer(@Autowired ApplicationEventPublisher applicationEventPublisher) {
    ListView<String> listView = new ListView<>();
    listView.setOnMouseClicked(
        event -> {
          applicationEventPublisher.publishEvent(
              new SwapFileOpenWindowEvent(
                  LocalState.getDeploymentState().getResult().stream()
                      .filter(
                          element ->
                              element
                                  .getName()
                                  .equals(listView.getSelectionModel().getSelectedItem()))
                      .toList()));
        });

    ElementStorage.setElement(id, listView);
    ElementStorage.setActualizable(this);
    ElementStorage.setResizable(this);
  }

  /**
   * @see IElement
   */
  @Override
  public ListView<String> getContent() {
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
    TopicLogsResult deploymentState = LocalState.getDeploymentState();

    Platform.runLater(
        () -> {
          if (!Objects.isNull(deploymentState)) {
            Set<String> items =
                deploymentState.getResult().stream()
                    .map(element -> new ListVisualizerCell(element.getName()))
                    .map(element -> element.getContent().getText())
                    .collect(Collectors.toSet());

            ObservableList<String> myObservableList =
                FXCollections.observableList(items.stream().toList());
            getContent().setItems(myObservableList);

            getContent().setMouseTransparent(false);
            getContent().setFocusTraversable(true);
          } else {
            List<String> items =
                Stream.of(new ListVisualizerCell(properties.getListViewStubName()))
                    .map(element -> element.getContent().getText())
                    .toList();

            ObservableList<String> myObservableList = FXCollections.observableList(items);
            getContent().setItems(myObservableList);

            getContent().setMouseTransparent(true);
            getContent().setFocusTraversable(false);
          }
        });
  }
}
