package com.repoachiever.service.scheduler.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

/** */
public class SchedulerStorage {
  private static final Map<UUID, ScheduledFuture<?>> storage = new HashMap<>();

  /**
   * @param id
   * @param element
   */
  public static void setThread(UUID id, ScheduledFuture<?> element) {
    storage.putIfAbsent(id, element);
  }

  /**
   * @param id
   * @return
   */
  public static ScheduledFuture<?> getThread(UUID id) {
    return storage.get(id);
  }
}
