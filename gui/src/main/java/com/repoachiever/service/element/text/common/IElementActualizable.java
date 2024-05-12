package com.repoachiever.service.element.text.common;

/** Contains additional method responsible for background updates handling. */
public interface IElementActualizable {
  /**
   * Represents handler for background updates, which will be running in the background. Should be
   * started in the constructor of the element.
   */
  void handleBackgroundUpdates();
}
