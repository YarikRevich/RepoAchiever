package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.File;
import java.util.List;

/** Represents swap file open window event used for state management. */
@Getter
public class OpenEvent extends ApplicationEvent implements IEvent {
  private final File configLocation;

  public OpenEvent(File configLocation) {
    super(configLocation);

    this.configLocation = configLocation;
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.OPEN_EVENT;
  }
}
