package com.repoachiever.service.element.common;

import com.repoachiever.service.element.storage.ElementStorage;
import java.util.Objects;
import java.util.UUID;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/** Represents helpful functionality, which is used for elements management. */
public class ElementHelper {
  /**
   * Executes scene switch. Thread-safe mode is provided.
   *
   * @param prev given previous scene.
   * @param next given next scene.
   */
  public static void switchScene(Scene prev, Scene next) {
    Platform.runLater(
        () -> {
          ((Stage) prev.getWindow()).setScene(next);
        });
  }

  /**
   * Checks if the given elements are equal.
   *
   * @param src1 given first object.
   * @param src2 given second object.
   */
  public static Boolean areElementsEqual(Object src1, Object src2) {
    UUID firstElementId = ElementStorage.getElementId(src1);

    if (Objects.isNull(firstElementId)) {
      return false;
    }

    UUID secondElementId = ElementStorage.getElementId(src2);

    if (Objects.isNull(secondElementId)) {
      return false;
    }

    return firstElementId.equals(secondElementId);
  }

  /**
   * Switches visibility of the given element.
   *
   * @param element element which visibility is intended to be changed.
   */
  public static void toggleElementVisibility(Node element) {
    element.getScene().getRoot().setDisable(!element.getScene().getRoot().isDisabled());
    element.setVisible(!element.isVisible());
  }

  /**
   * Enables presentation of the given alert with the given content. Thread-safe mode is provided.
   *
   * @param alert given alert to be presented.
   * @param message given message to be used as a content.
   */
  public static void showAlert(Alert alert, String message) {
    Platform.runLater(
        () -> {
          String prevText = alert.getContentText();

          alert.setContentText(message);
          alert.show();
          alert.setOnHiding(
              event -> {
                alert.setContentText(prevText);
                alert.setOnHiding(null);
              });
        });
  }

  /** Retrieves */
  public static Point2D getCentralPoint(Double width, Double height) {
    return new Point2D(width / 2, height / 2);
  }

  public static Rectangle2D getSizeWithScale(
      Double windowWidth, Double windowHeight, Double widthScale, Double heightScale) {
    return new Rectangle2D(0, 0, windowWidth * widthScale, windowHeight * heightScale);
  }

  public static Double getCircularElementSize(Double windowWidth, Double scale) {
    return windowWidth * scale;
  }
}
