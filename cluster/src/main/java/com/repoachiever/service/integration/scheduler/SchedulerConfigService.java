package com.repoachiever.service.integration.scheduler;

import com.repoachiever.converter.CronExpressionConverter;
import com.repoachiever.exception.CronExpressionException;
import com.repoachiever.exception.SchedulerPeriodRetrievalFailureException;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service used to perform RepoAchiever Cluster worker configuration operations.
 */
@Component
public class SchedulerConfigService {
    private static final Logger logger = LogManager.getLogger(SchedulerConfigService.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private VendorFacade vendorFacade;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

    /**
     * Performs configuration of RepoAchiever Cluster workers.
     *
     * @throws SchedulerPeriodRetrievalFailureException if scheduler period retrieval process failed.
     */
    @PostConstruct
    private void process() throws SchedulerPeriodRetrievalFailureException {
        Long period;

        try {
            period = CronExpressionConverter.convert(
                    configService.getConfig().getResource().getWorker().getFrequency());
        } catch (CronExpressionException e) {
            throw new SchedulerPeriodRetrievalFailureException(e.getMessage());
        }

        configService.getConfig().getFilter().getLocations().forEach(element -> {
            logger.info(
                    LoggingConfigurationHelper.getTransferableMessage(
                            String.format("Starting worker for '%s' location", element)));

            scheduledExecutorService.scheduleAtFixedRate(() -> {


            }, 0, period, TimeUnit.MILLISECONDS);
        });
    }
}
