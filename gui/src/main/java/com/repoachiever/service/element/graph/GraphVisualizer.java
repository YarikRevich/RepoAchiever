package com.repoachiever.service.element.graph;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.SmartGraphCssFileNotFoundException;
import com.repoachiever.exception.SmartGraphPropertiesFileNotFoundException;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import com.repoachiever.service.element.text.common.IElementResizable;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphVisualizer
    implements IElementResizable, IElement<SmartGraphPanel<String, String>> {
  private final UUID id = UUID.randomUUID();

  public GraphVisualizer(@Autowired PropertiesEntity properties)
      throws SmartGraphCssFileNotFoundException, SmartGraphPropertiesFileNotFoundException {
    Graph<String, String> g = new GraphEdgeList<>();
    g.insertVertex("A");
    g.insertVertex("B");
    g.insertVertex("C");
    g.insertVertex("D");
    g.insertVertex("E");
    g.insertVertex("F");
    g.insertVertex("G");

    g.insertEdge("A", "B", "1");
    g.insertEdge("A", "C", "2");
    g.insertEdge("A", "D", "3");
    g.insertEdge("A", "E", "4");
    g.insertEdge("A", "F", "5");
    g.insertEdge("A", "G", "6");

    g.insertVertex("H");
    g.insertVertex("I");
    g.insertVertex("J");
    g.insertVertex("K");
    g.insertVertex("L");
    g.insertVertex("M");
    g.insertVertex("N");

    g.insertEdge("H", "I", "7");
    g.insertEdge("H", "J", "8");
    g.insertEdge("H", "K", "9");
    g.insertEdge("H", "L", "10");
    g.insertEdge("H", "M", "11");
    g.insertEdge("H", "N", "12");

    g.insertEdge("A", "H", "0");

    SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();

    ClassLoader classLoader = getClass().getClassLoader();

    InputStream smartGraphPropertiesInputStream =
        classLoader.getResourceAsStream(properties.getGraphPropertiesLocation());
    if (Objects.isNull(smartGraphPropertiesInputStream)) {
      throw new SmartGraphPropertiesFileNotFoundException();
    }

    SmartGraphProperties smartGraphProperties =
        new SmartGraphProperties(smartGraphPropertiesInputStream);

    URL smartGraphCssFileLocationURL =
        classLoader.getResource(properties.getGraphCssFileLocation());
    if (Objects.isNull(smartGraphCssFileLocationURL)) {
      throw new SmartGraphCssFileNotFoundException();
    }

    URI smartGraphCssFileLocationURI;
    try {
      smartGraphCssFileLocationURI = smartGraphCssFileLocationURL.toURI();
    } catch (URISyntaxException e) {
      throw new SmartGraphCssFileNotFoundException(e.getMessage());
    }

    ElementStorage.setElement(
        id, new SmartGraphPanel<>(g, smartGraphProperties, strategy, smartGraphCssFileLocationURI));
  }

  /**
   * @return
   */
  @Override
  public SmartGraphPanel<String, String> getContent() {
    return ElementStorage.getElement(id);
  }

  /** */
  @Override
  public void handlePrefWidth() {}

  /** */
  @Override
  public void handlePrefHeight() {}
}
