package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents main window height update event used for state management. */
@Getter
public class MainWindowHeightUpdateEvent extends ApplicationEvent implements IEvent {
  private final Double height;

  public MainWindowHeightUpdateEvent(Double height) {
    super(height);

    this.height = height;
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.MAIN_WINDOW_HEIGHT_UPDATE_EVENT;
  }
}
