package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


/** Represents clean event used for state management. */
@Getter
public class CleanEvent extends ApplicationEvent implements IEvent {
    private final String location;

    public CleanEvent(String location) {
        super(location);

        this.location = location;
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.CLEAN_EVENT;
    }
}
