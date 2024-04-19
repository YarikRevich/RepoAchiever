package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import org.springframework.context.ApplicationEvent;

/** */
public class EditorOpenWindowEvent extends ApplicationEvent implements IEvent {
  public EditorOpenWindowEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.EDITOR_OPEN_WINDOW_EVENT;
  }
}
