package com.repoachiever.service.cluster.common;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * Contains helpful tools used for RepoAchiever Cluster configuration.
 */
public class ClusterConfigurationHelper {
    private final static ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

    /**
     * Composes name for RepoAchiever Cluster using pre-defined prefix and UUID.
     *
     * @param prefix given name prefix.
     * @return composed RepoAchiever Cluster name.
     */
    public static String getName(String prefix) {
        return String.format("%s-%s", prefix, UUID.randomUUID());
    }

    /**
     * Waits till the given callback execution succeeds.
     *
     * @param callback given callback.
     * @param frequency given callback execution check frequency.
     * @param timeout given callback execution timeout.
     * @return result of the execution.
     */
    public static Boolean waitForStart(Callable<Boolean> callback, Integer frequency, Integer timeout) {
        CountDownLatch waiter = new CountDownLatch(1);

        ScheduledFuture<?> awaitTask = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (callback.call()) {
                    waiter.countDown();
                }
            } catch (Exception ignore) {
            }
        }, 0, frequency, TimeUnit.MILLISECONDS);

        ScheduledFuture<?> timeoutTask = scheduledExecutorService.schedule(() -> {
            if (!awaitTask.isCancelled()) {
                awaitTask.cancel(true);

                waiter.countDown();
            }
        }, timeout, TimeUnit.MILLISECONDS);


        try {
            waiter.await();
        } catch (InterruptedException e) {
            return false;
        }

        if (!awaitTask.isCancelled()) {
            awaitTask.cancel(true);
        } else {
            return false;
        }

        if (!timeoutTask.isDone()) {
            timeoutTask.cancel(true);
        }

        return true;
    }
}
