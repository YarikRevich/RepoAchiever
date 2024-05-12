package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents main window width update event used for state management. */
@Getter
public class MainWindowWidthUpdateEvent extends ApplicationEvent implements IEvent {
  private final Double width;

  public MainWindowWidthUpdateEvent(Double width) {
    super(width);

    this.width = width;
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.MAIN_WINDOW_WIDTH_UPDATE_EVENT;
  }
}
