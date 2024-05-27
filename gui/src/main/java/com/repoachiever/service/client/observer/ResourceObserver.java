package com.repoachiever.service.client.observer;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.HealthCheckResult;
import com.repoachiever.service.client.command.HealthCheckClientCommandService;
import com.repoachiever.service.client.command.ReadinessCheckClientCommandService;
import com.repoachiever.service.event.payload.ConnectionStatusEvent;
import com.repoachiever.service.hand.executor.CommandExecutorService;
import com.repoachiever.service.scheduler.SchedulerHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/** Provides resource observables to manage state of the application. */
@Component
public class ResourceObserver {
  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  @Autowired private PropertiesEntity properties;

  @Autowired private CommandExecutorService commandExecutorService;

  @Autowired private HealthCheckClientCommandService healthCommandService;

  /** Sends healthcheck requests to API Server and updates connection status. */
  @PostConstruct
  private void handleHealthCommand() {
    SchedulerHelper.scheduleTask(
        () -> {
          ConnectionStatusEvent connectionStatusEvent;

          try {
            HealthCheckResult result = healthCommandService.process(null);

            connectionStatusEvent =
                switch (result.getStatus()) {
                  case UP -> new ConnectionStatusEvent(true);
                  case DOWN -> new ConnectionStatusEvent(false);
                };
          } catch (ApiServerOperationFailureException e) {
            connectionStatusEvent = new ConnectionStatusEvent(false);
          }

          applicationEventPublisher.publishEvent(connectionStatusEvent);
        },
        properties.getProcessHealthcheckPeriod());
  }
}
