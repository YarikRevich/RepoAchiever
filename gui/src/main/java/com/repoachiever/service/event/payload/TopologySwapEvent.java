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
    private final List<TopologyInfoUnit> content;

    public TopologySwapEvent(List<TopologyInfoUnit> content) {
        super(content);

        this.content = content;
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.TOPOLOGY_SWAP_EVENT;
    }
}
