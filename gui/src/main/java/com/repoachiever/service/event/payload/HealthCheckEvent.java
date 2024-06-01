package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents health check status event used for state management. */
@Getter
public class HealthCheckEvent extends ApplicationEvent implements IEvent {
  public HealthCheckEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.HEALTH_CHECK_EVENT;
  }
}
