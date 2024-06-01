package com.repoachiever.service.event.payload;

import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.model.ContentRetrievalUnit;
import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents details swap file open window event used for state management. */
@Getter
public class ContentSwapEvent extends ApplicationEvent implements IEvent {
  private final ContentRetrievalUnit content;

  public ContentSwapEvent(ContentRetrievalUnit content) {
    super(content);

    this.content = content;
  }

  /**
   * @see IEvent
   */
  public EventType getEventType() {
    return EventType.DETAILS_SWAP_EVENT;
  }
}
