package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/** Represents topology event used for state management. */
@Getter
public class TopologyEvent extends ApplicationEvent implements IEvent {
    public TopologyEvent() {
        super(new Object());
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.TOPOLOGY_EVENT;
    }
}
