package com.repoachiever.service.event.common;

/** Represents general internal event system signature. */
public interface IEvent {
  /**
   * Returns current event type.
   *
   * @return event type.
   */
  EventType getEventType();
}
