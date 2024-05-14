package com.repoachiever.service.visualization.common;

/** Represents iterative interface for visualization label. */
public interface IVisualizationLabel {
  /**
   * Checks if there are steps available in the storage.
   *
   * @return result of the check.
   */
  Boolean isEmpty();

  /**
   * Checks if there is next step in the specified label.
   *
   * @return result of the check.
   */
  Boolean isNext();

  /** Pushes next step in the specified label. */
  void pushNext();

  /**
   * Returns string interpretation of the current step.
   *
   * @return string interpretation of the current step.
   */
  String getCurrent();
}
