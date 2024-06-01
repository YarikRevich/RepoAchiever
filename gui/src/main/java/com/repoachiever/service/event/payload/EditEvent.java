package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import org.springframework.context.ApplicationEvent;

/** Represents edit event used for state management. */
public class EditEvent extends ApplicationEvent implements IEvent {
  public EditEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.EDIT_EVENT;
  }
}
