package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents connection status event used for state management. */
@Getter
public class ConnectionStatusEvent extends ApplicationEvent implements IEvent {
  private final boolean connectionEstablished;

  public ConnectionStatusEvent(boolean connectionEstablished) {
    super(connectionEstablished);

    this.connectionEstablished = connectionEstablished;
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.CONNECTION_STATUS_EVENT;
  }
}
