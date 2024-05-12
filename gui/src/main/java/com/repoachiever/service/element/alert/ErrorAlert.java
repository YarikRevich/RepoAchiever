package com.repoachiever.service.element.alert;

import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import ink.bluecloud.css.CssResources;
import ink.bluecloud.css.ElementButton;
import ink.bluecloud.css.ElementButtonKt;
import java.util.UUID;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.springframework.stereotype.Service;

/** Represents error related alert. */
@Service
public class ErrorAlert implements IElement<Alert> {
  UUID id = UUID.randomUUID();

  public ErrorAlert() {
    Platform.runLater(
        () -> {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error occurred");

          ElementButtonKt.theme(
              (Button) alert.getDialogPane().lookupButton(ButtonType.OK), ElementButton.redButton);
          alert.getDialogPane().getStylesheets().add(CssResources.globalCssFile);
          alert.getDialogPane().getStylesheets().add(CssResources.buttonCssFile);
          alert.getDialogPane().getStylesheets().add(CssResources.textFieldCssFile);

          ElementStorage.setElement(id, alert);
        });
  }

  /**
   * @see IElement
   */
  @Override
  public Alert getContent() {
    return ElementStorage.getElement(id);
  }
}
