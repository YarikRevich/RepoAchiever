package com.repoachiever.service.state;

import lombok.Getter;
import lombok.Setter;

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
