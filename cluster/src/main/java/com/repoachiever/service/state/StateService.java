package com.repoachiever.service.state;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Service used to operate as a collection of application state properties.
 */
public class StateService {
    /**
     * Represents exit state used to indicate requested application shutdown.
     */
    @Getter
    @Setter
    private static Boolean exit = false;

    /**
     * Represents suspended state used to temporary halt execution of RepoAchiever Cluster allocation. By default
     * RepoAchiever Cluster is considered to be suspended.
     */
    @Getter
    @Setter
    private static Boolean suspended = true;

    /**
     * Represents a set of content updates head counter, pointing at the latest scanned head of the stream for the
     * provided content locations. As keys there are used provided location names and as values commit amounts are used.
     */
    @Getter
    private final static Map<String, Integer> contentUpdatesHeadCounterSet = new HashMap<>();

    /**
     * Checks if current content update head counter for the given location name is below or equal to the given value.
     *
     * @return result of the check.
     */
    public static Boolean isContentUpdateHeadCounterBelow(String location, Integer value) {
        return contentUpdatesHeadCounterSet.getOrDefault(location, 0) < value;
    }

    /**
     * Represents log message queue used to handle RepoAchiever API Server log message transfer.
     */
    @Getter
    private final static ConcurrentLinkedQueue<String> logMessagesQueue = new ConcurrentLinkedQueue<>();

    /**
     * Adds new log message to log message queue.
     *
     * @param message given log message to be added.
     */
    public static void addLogMessage(String message) {
        logMessagesQueue.add(message);
    }
}
