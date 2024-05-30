package com.repoachiever.service.event.payload;

import com.repoachiever.model.ContentUnit;
import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import java.util.List;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents swap file open window event used for state management. */
@Getter
public class SwapEvent extends ApplicationEvent implements IEvent {
  private final List<ContentUnit> content;

  public SwapEvent(List<ContentUnit> content) {
    super(content);

    this.content = content;
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.SWAP_EVENT;
  }
}
