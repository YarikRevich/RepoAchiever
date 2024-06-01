package com.repoachiever.service.scheduler;

import java.util.concurrent.*;

/**
 * Contains helpful tools used for scheduler configuration.
 */
public class SchedulerConfigurationHelper {
  private static final ScheduledExecutorService scheduledExecutorService =
          Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

  private static final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


  /**
   * Schedules given task with the specified period.
   *
   * @param callback task to be scheduled.
   * @param period period of the task execution.
   */
  public static void scheduleTask(Runnable callback, Integer period) {
    scheduledExecutorService.scheduleWithFixedDelay(
                () -> executorService.execute(callback), 0, period, TimeUnit.MILLISECONDS);
  }

  /**
   * Schedules given task with the specified delay.
   *
   * @param callback task to be scheduled.
   * @param delay delay of the task execution.
   */
  public static void scheduleTimer(Runnable callback, Integer delay) {
    scheduledExecutorService.schedule(
        () -> executorService.execute(callback), delay, TimeUnit.MILLISECONDS);
  }

  /**
   * Schedules given task with immediately.
   *
   * @param callback task to be scheduled.
   */
  public static void scheduleOnce(Runnable callback) {
    scheduleTimer(callback, 0);
  }

  /** Closes schedulers and finishes awaited tasks. */
  public static void close() {
    scheduledExecutorService.close();
    executorService.close();
  }
}
