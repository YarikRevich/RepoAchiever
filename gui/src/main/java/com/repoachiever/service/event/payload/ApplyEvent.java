package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents apply event used for state management. */
@Getter
public class ApplyEvent extends ApplicationEvent implements IEvent {
  public ApplyEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.APPLY_EVENT;
  }
}
