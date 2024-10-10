package com.repoachiever.service.state;

import com.repoachiever.dto.SuspenderDto;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
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
    private final static AtomicBoolean suspended = new AtomicBoolean();

    /**
     * Represents vendor availability state used to identify timeout restrictions for external API calls.
     */
    @Getter
    private static AtomicBoolean vendorAvailability = new AtomicBoolean(true);

    /**
     * Represents a list of suspenders, which are used to control content retrieval process.
     */
    @Getter
    private final static List<SuspenderDto> suspenders = new ArrayList<>();

    /**
     * Adds new RepoAchiever Cluster suspender.
     *
     * @param name given RepoAchiever Cluster suspender name.
     */
    public static void addSuspender(String name) {
        suspenders.add(SuspenderDto.of(name, new AtomicReference<>(new CountDownLatch(0))));
    }

    /**
     * Removes RepoAchiever Cluster suspender with the given name.
     *
     * @param name given RepoAchiever Cluster suspender name.
     */
    public static void removeSuspenderByName(String name) {
        suspenders.removeIf(element -> Objects.equals(element.getName(), name));
    }

    /**
     * Resets RepoAchiever Cluster suspender by the given name.
     *
     * @param name given RepoAchiever Cluster suspender name.
     */
    public static void resetSuspenderByName(String name) {
        suspenders
                .stream()
                .filter(element -> Objects.equals(element.getName(), name))
                .toList()
                .getFirst()
                .getAwaiter()
                .set(new CountDownLatch(1));
    }

    /**
     * Counts down RepoAchiever Cluster suspender suspender by the given name.
     *
     * @param name given RepoAchiever Cluster suspender name.
     */
    public static void countDownSuspenderByName(String name) {
        suspenders
                .stream()
                .filter(element -> Objects.equals(element.getName(), name))
                .toList()
                .getFirst()
                .getAwaiter()
                .get()
                .countDown();
    }


    /**
     * Represents RepoAchiever Cluster suspend state guard.
     */
    @Getter
    private final static ReentrantLock suspendGuard = new ReentrantLock();

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
