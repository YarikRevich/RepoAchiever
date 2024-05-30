package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents retrieve content event used for state management. */
@Getter
public class RetrieveContentEvent extends ApplicationEvent implements IEvent {
  public RetrieveContentEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.RETRIEVE_CONTENT_EVENT;
  }
}
