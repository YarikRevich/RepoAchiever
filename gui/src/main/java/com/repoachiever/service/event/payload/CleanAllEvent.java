package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


/** Represents cleanall event used for state management. */
@Getter
public class CleanAllEvent extends ApplicationEvent implements IEvent {
    public CleanAllEvent() {
        super(new Object());
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.CLEAN_ALL_EVENT;
    }
}
