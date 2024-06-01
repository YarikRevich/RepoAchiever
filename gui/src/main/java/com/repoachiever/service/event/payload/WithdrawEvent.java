package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents withdraw event used for state management. */
@Getter
public class WithdrawEvent extends ApplicationEvent implements IEvent {
  public WithdrawEvent() {
    super(new Object());
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.WITHDRAW_EVENT;
  }
}
