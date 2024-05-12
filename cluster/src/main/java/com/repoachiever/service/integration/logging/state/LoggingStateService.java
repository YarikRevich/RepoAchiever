package com.repoachiever.service.integration.logging.state;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.state.StateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Service used to handle incoming logging related ąpplication state changes.
 */
@Component
public class LoggingStateService {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PropertiesEntity properties;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(2);

    /**
     * Performs application exit if the required state has been changed.
     */
    @PostConstruct
    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (StateService.getExit()) {
                CountDownLatch finalizer = new CountDownLatch(1);

                ScheduledFuture<?> finalizerFeature =
                        scheduledExecutorService.scheduleAtFixedRate(() -> {
                                    if (StateService.getLogMessagesQueue().isEmpty()) {
                                        finalizer.countDown();
                                    }
                                }, 0,
                                properties.getLoggingStateFinalizerFrequency(),
                                TimeUnit.MILLISECONDS);

                try {
                    finalizer.await();
                } catch (InterruptedException ignored) {
                }

                finalizerFeature.cancel(true);

                ((ConfigurableApplicationContext) applicationContext).close();
                System.exit(1);
            }
        }, 0, properties.getLoggingStateFrequency(), TimeUnit.MILLISECONDS);
    }
}
