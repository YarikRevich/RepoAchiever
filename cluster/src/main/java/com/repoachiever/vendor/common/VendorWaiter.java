package com.repoachiever.vendor.common;

import lombok.SneakyThrows;

import java.util.concurrent.*;
import java.util.function.Supplier;

/** Represents readiness waiter used for to wait for resource availability. */
public class VendorWaiter {
  private static final ScheduledExecutorService scheduledExecutorService =
      Executors.newScheduledThreadPool(4);

  /**
   * Waits for the given condition to succeed.
   *
   * @param callback given callback to be executed.
   * @param period given period to be used for scheduler.
   */
  @SneakyThrows
  public static void awaitFor(Supplier<Boolean> callback, Integer period) {
    CountDownLatch latch = new CountDownLatch(1);

    ScheduledFuture<?> awaitTask =
        scheduledExecutorService.scheduleAtFixedRate(
            () -> {
                if (latch.getCount() == 0){
                    return;
                }

              if (callback.get()) {
                latch.countDown();
              }
            },
            0,
            period,
            TimeUnit.MILLISECONDS);

    latch.await();

    awaitTask.cancel(true);
  }
}
