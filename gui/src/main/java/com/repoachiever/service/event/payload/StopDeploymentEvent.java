package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents stop deployment event used for state management. */
@Getter
public class StopDeploymentEvent extends ApplicationEvent implements IEvent {
  public StopDeploymentEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.STOP_DEPLOYMENT_EVENT;
  }
}
