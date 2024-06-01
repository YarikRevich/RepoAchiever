package com.repoachiever.service.event.payload;

import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/** Represents topology swap file open window event used for state management. */
@Getter
public class TopologySwapEvent extends ApplicationEvent implements IEvent {
    public TopologySwapEvent() {
        super(new Object());
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.TOPOLOGY_SWAP_EVENT;
    }
}
