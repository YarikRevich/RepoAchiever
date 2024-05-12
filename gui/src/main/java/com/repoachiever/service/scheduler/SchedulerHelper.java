package com.repoachiever.service.scheduler;

import com.repoachiever.service.scheduler.storage.SchedulerStorage;
import java.util.UUID;
import java.util.concurrent.*;

/** Provides access to task schedule opportunities. */
public class SchedulerHelper {
  private static final ScheduledExecutorService scheduledExecutorService =
      Executors.newScheduledThreadPool(8);

  // TODO: Make it possible to configure thread pool from properties.
  private static final ExecutorService executorService = Executors.newScheduledThreadPool(8);

  /**
   * Schedules given task with the specified period.
   *
   * @param callback task to be scheduled.
   * @param period period of the task execution.
   */
  public static void scheduleTask(Runnable callback, Integer period) {
    ScheduledFuture<?> element =
        scheduledExecutorService.scheduleAtFixedRate(
                new Runnable() {
                  @Override
                  public void run() {
                    executorService.execute(callback);
                  }
                }, 0, period, TimeUnit.MILLISECONDS);

    SchedulerStorage.setThread(UUID.randomUUID(), element);
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
