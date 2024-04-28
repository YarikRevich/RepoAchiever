package com.repoachiever.service.integration.logging;

import com.repoachiever.service.state.StateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service used to handle incoming logging related ąpplication state changes.
 */
@Component
public class LoggingStateConfigService {
    @Autowired
    private ApplicationContext applicationContext;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    /**
     * Performs application exit if the required state has been changed.
     */
    @PostConstruct
    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (StateService.getExit()) {
                ((ConfigurableApplicationContext) applicationContext).close();
                System.exit(1);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
}
