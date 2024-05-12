package com.repoachiever.service.element.image.collection;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/** Represents connection status image collection used for state indication. */
public class ConnectionStatusImageCollection {
  /**
   * Retrieves image, which represents successful connection status.
   *
   * @return shape, which represents successful connection status image.
   */
  public static Shape getSuccessfulConnectionStatusImage(Double size) {
    return new Circle(size, size, size, Color.GREEN);
  }

  /**
   * Retrieves image, which represents failed connection status.
   *
   * @return shape, which represents failed connection status image.
   */
  public static Shape getFailedConnectionStatusImage(Double size) {
    return new Circle(size, size, size, Color.RED);
  }
}
