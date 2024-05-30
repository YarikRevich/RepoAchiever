package com.repoachiever.service.element.menu;

import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElement;
import java.util.UUID;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import org.springframework.stereotype.Service;

/** Represents tab menu bar. */
@Service
public class TabMenuBar implements IElement<MenuBar> {
  private final UUID id = UUID.randomUUID();

  public TabMenuBar() {
    Menu preferenciesMenu = new Menu("Preferences");
    Menu helpMenu = new Menu("Help");

    MenuBar menuBar = new MenuBar();

    menuBar.getMenus().addAll(preferenciesMenu, helpMenu);
    menuBar.useSystemMenuBarProperty().set(true);

    ElementStorage.setElement(id, menuBar);
  }

  public MenuBar getContent() {
    return ElementStorage.getElement(id);
  }
}
