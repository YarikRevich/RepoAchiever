package com.repoachiever.service.visualization;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.visualization.state.VisualizationState;

import java.util.concurrent.*;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents visualization service used to indicate current execution steps.
 */
@Service
public class VisualizationService {
    private static final Logger logger = LogManager.getLogger(VisualizationService.class);

    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private VisualizationState visualizationState;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * Starts progress visualization processor.
     */
    public void process() {
        scheduledExecutorService.scheduleAtFixedRate(
                () -> {
                    if (visualizationState.getLabel().isNext()) {
                        logger.info(visualizationState.getLabel().getCurrent());
                    }

                    if (visualizationState.getLabel().isEmpty() && !visualizationState.getLabel().isNext()) {
                        latch.countDown();
                    }
                },
                0,
                properties.getProgressVisualizationPeriod(),
                TimeUnit.MILLISECONDS);
    }

    /**
     * Awaits for visualization service to end its processes.
     */
    @SneakyThrows
    public void await() {
        latch.await();

        if (!visualizationState.getResult().isEmpty()) {
            System.out.print("\n");
        }

        visualizationState.getResult().forEach(logger::info);
    }
}
