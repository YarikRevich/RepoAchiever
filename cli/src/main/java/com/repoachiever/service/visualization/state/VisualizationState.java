package com.repoachiever.service.visualization.state;

import com.repoachiever.service.visualization.common.IVisualizationLabel;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * Represents general visualization state used to gather output values to be processed by
 * visualization service.
 */
@Getter
@Service
public class VisualizationState {
  @Setter private IVisualizationLabel label;

  private final List<String> result = new ArrayList<>();

  /**
   * Adds given message to the result array.
   *
   * @param message given message to be added to result array.
   */
  public void addResult(String message) {
    result.add(message);
  }
}
