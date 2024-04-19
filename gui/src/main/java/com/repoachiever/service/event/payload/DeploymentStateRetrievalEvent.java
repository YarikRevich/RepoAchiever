package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents status update event used for state management. */
@Getter
public class DeploymentStateRetrievalEvent extends ApplicationEvent implements IEvent {
  public DeploymentStateRetrievalEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.DEPLOYMENT_STATE_RETRIEVAL_EVENT;
  }
}
