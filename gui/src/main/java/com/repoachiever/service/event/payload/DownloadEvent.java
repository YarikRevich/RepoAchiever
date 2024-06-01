package com.repoachiever.service.event.payload;

import com.repoachiever.service.event.common.EventType;
import com.repoachiever.service.event.common.IEvent;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.File;


/** Represents download event used for state management. */
@Getter
public class DownloadEvent extends ApplicationEvent implements IEvent {
    private final String location;

    private final File destination;

    public DownloadEvent(String location, File destination) {
        super(location);

        this.location = location;
        this.destination = destination;
    }

    /**
     * @see IEvent
     */
    public EventType getEventType() {
        return EventType.DOWNLOAD_EVENT;
    }
}
