package com.repoachiever.service.integration.scheduler;

import com.repoachiever.converter.CronExpressionConverter;
import com.repoachiever.exception.*;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.apiserver.resource.ApiServerCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

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

    @Autowired
    private ApiServerCommunicationResource apiServerCommunicationResource;

    private final ScheduledExecutorService starterScheduledExecutorService =
            Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());

    private final ScheduledExecutorService operationScheduledExecutorService =
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

            starterScheduledExecutorService.execute(() -> {
                CountDownLatch closable = new CountDownLatch(1);

                ScheduledFuture<?> execution = operationScheduledExecutorService.scheduleAtFixedRate(() -> {
                    Integer commitAmount;

                    try {
                        commitAmount = vendorFacade.getCommitAmount(element);
                    } catch (LocationDefinitionsAreNotValidException e) {
                        logger.info(
                                LoggingConfigurationHelper.getTransferableMessage(
                                        String.format(
                                                "Skipping retrieval of content for '%s' location: %s",
                                                element,
                                                e.getMessage())));
                        closable.countDown();

                        return;
                    } catch (GitHubContentRetrievalFailureException e) {
                        logger.info(
                                LoggingConfigurationHelper.getTransferableMessage(
                                        String.format(
                                                "Failed to retrieve content for '%s' location: %s",
                                                element,
                                                e.getMessage())));
                        return;
                    }

                    if (StateService.isContentUpdateHeadCounterBelow(element, commitAmount)) {
                        String commitHash;

                        try {
                            commitHash = vendorFacade.getLatestCommitHash(element);
                        } catch (LocationDefinitionsAreNotValidException ignored) {
                            return;
                        } catch (GitHubContentRetrievalFailureException e) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Failed to retrieve content for '%s' location: %s",
                                                    element,
                                                    e.getMessage())));
                            return;
                        }

                        StateService.getContentUpdatesHeadCounterSet().put(element, commitAmount);

                        Boolean contentPresent;

                        try {
                            contentPresent =
                                    apiServerCommunicationResource.retrieveRawContentPresent(element, commitHash);
                        } catch (ApiServerOperationFailureException e) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Failed to retrieve state for '%s' location: %s",
                                                    element,
                                                    e.getMessage())));
                            return;
                        }

                        if (!contentPresent) {

                        }
                    }
                }, 0, period, TimeUnit.MILLISECONDS);

                try {
                    closable.await();
                } catch (InterruptedException e) {
                    logger.fatal(e.getMessage());
                }

                execution.cancel(true);
            });
        });
    }
}
