package com.repoachiever.service.integration.apiserver.healthcheck;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.event.payload.HealthCheckEvent;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.executor.CommandExecutorService;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Service used to perform RepoAchiever GUI health check operations.
 */
@Component
public class ApiServerHealthCheckService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private CommandExecutorService commandExecutorService;

    /**
     * Sends healthcheck requests to RepoAchiever API Server and updates connection status.
     */
    @PostConstruct
    private void handleHealthCommand() {
        SchedulerConfigurationHelper.scheduleTask(
                () -> {
                    if (Objects.nonNull(StateService.getConfigLocation())) {
                        applicationEventPublisher.publishEvent(new HealthCheckEvent());
                    }
                },
                properties.getProcessHealthcheckPeriod());
    }
}
