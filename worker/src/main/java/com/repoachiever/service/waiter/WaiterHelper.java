package com.repoachiever.service.waiter;

import java.util.concurrent.CountDownLatch;

/** Represents waiter helper for general usage. */
public class WaiterHelper {
  private static final CountDownLatch latch = new CountDownLatch(1);

  /** Indefinitely waits for manual program execution. */
  public static void waitForExit() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
