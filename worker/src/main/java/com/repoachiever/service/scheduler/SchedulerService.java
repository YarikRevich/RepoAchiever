package com.repoachiever.service.scheduler;

import com.repoachiever.converter.CronExpressionConverter;
import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.exception.CommandExecutorException;
import com.repoachiever.exception.CronExpressionException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.scheduler.command.ExecCommandService;
import com.repoachiever.service.scheduler.executor.CommandExecutorService;
import com.repoachiever.service.waiter.WaiterHelper;
import jakarta.annotation.PreDestroy;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Exposes opportunity to schedule incoming requests. */
@Service
public class SchedulerService {
  @Autowired private ConfigService configService;

  @Autowired private CommandExecutorService commandExecutorService;

  private final ScheduledExecutorService scheduledExecutorService =
      Executors.newSingleThreadScheduledExecutor();

  private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

  /**
   * Starts executor listening process, which receives incoming requests and executes them with the
   * help of local command executor service.
   */
  public void start() {
    configService.getConfig().getRequests().parallelStream()
        .forEach(
            request -> {
              long period;
              try {
                period = CronExpressionConverter.convert(request.getFrequency());
              } catch (CronExpressionException e) {
                throw new RuntimeException(e);
              }

              scheduledExecutorService.scheduleAtFixedRate(
                      register(request.getName(), request.getScript()),
                  0,
                  period,
                  TimeUnit.MILLISECONDS);
            });

    WaiterHelper.waitForExit();
  }

    /**
     * Registers the execution of the given request.
     *
     * @param name name of the request to be processed.
     * @param input script to be executed.
     * @return execution callback.
     */
  private Runnable register(String name, String input) {
      return () -> {
          CountDownLatch latch = new CountDownLatch(1);

          executorService.execute(
                  () -> {
                      try {
                          exec(name, input);
                      } catch (CommandExecutorException e) {

                          throw new RuntimeException(e);
                      }

                      latch.countDown();
                  });

          try {
              latch.await();
          } catch (InterruptedException e) {
              throw new RuntimeException(e);
          }
      };
  }

  /**
   * Executes given script and sends result as message to Kafka cluster.
   *
   * @param name name of the request to be processed.
   * @param input script to be executed.
   */
  private void exec(String name, String input) throws CommandExecutorException {
    ExecCommandService execCommandService = new ExecCommandService(input);

    CommandExecutorOutputDto scriptExecCommandOutput =
        commandExecutorService.executeCommand(execCommandService);
  }

  @PreDestroy
  private void close() {
    scheduledExecutorService.shutdown();

    try {
      if (!scheduledExecutorService.awaitTermination(1000, TimeUnit.HOURS)) {
        scheduledExecutorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      scheduledExecutorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
