package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


/** Represents download event used for state management. */
@Getter
public class DownloadEvent extends ApplicationEvent implements IEvent {
    private final String location;

    public DownloadEvent(String location) {
        super(location);

        this.location = location;
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.DOWNLOAD_EVENT;
    }
}
