package com.repoachiever.service.integration.apiserver.healthcheck;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.executor.CommandExecutorService;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Service used to perform RepoAchiever Cluster health check operations.
 */
@Component
public class ApiServerHealthCheckService {
  @Autowired
  private PropertiesEntity properties;

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  @Autowired
  private CommandExecutorService commandExecutorService;

  /** Sends healthcheck requests to API Server and updates connection status. */
  @PostConstruct
  private void handleHealthCommand() {
    SchedulerConfigurationHelper.scheduleTask(
        () -> {

//          ConnectionStatusEvent connectionStatusEvent;
//
//          try {
//            HealthCheckResult result = healthCommandService.process(null);
//
//            connectionStatusEvent =
//                switch (result.getStatus()) {
//                  case UP -> new ConnectionStatusEvent(true);
//                  case DOWN -> new ConnectionStatusEvent(false);
//                };
//          } catch (ApiServerOperationFailureException e) {
//            connectionStatusEvent = new ConnectionStatusEvent(false);
//          }
//
//          applicationEventPublisher.publishEvent(connectionStatusEvent);
        },
        properties.getProcessHealthcheckPeriod());
  }
}
