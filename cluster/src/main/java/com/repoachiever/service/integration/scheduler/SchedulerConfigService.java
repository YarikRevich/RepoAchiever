package com.repoachiever.service.integration.scheduler;

import com.repoachiever.converter.CronExpressionConverter;
import com.repoachiever.dto.AdditionalContentDto;
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
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;

import java.util.Objects;
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

    private final ExecutorService starterExecutorService = Executors.newVirtualThreadPerTaskExecutor();

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

        configService.getConfig().getContent().getLocations().forEach(element -> {
            logger.info(
                    LoggingConfigurationHelper.getTransferableMessage(
                            String.format(
                                    "Starting worker for '%s(additional: %b)' location",
                                    element.getName(),
                                    element.getAdditional())));

            StateService.addSuspender(element.getName());

            starterExecutorService.execute(() -> {
                CountDownLatch closable = new CountDownLatch(1);

                ScheduledFuture<?> execution = operationScheduledExecutorService.scheduleWithFixedDelay(() -> {
                    if (StateService.getSuspended().get()) {
                        logger.info(
                                LoggingConfigurationHelper.getTransferableMessage(
                                        String.format("Skipping execution for '%s' location due to suspension",
                                                element.getName())
                                ));

                        return;
                    }

                    try {
                        logger.info(
                                LoggingConfigurationHelper.getTransferableMessage(
                                        String.format("Entering execution for '%s' location", element.getName())));

                        StateService.resetSuspenderByName(element.getName());

                        CountDownLatch awaiter = StateService.getSuspenderAwaiterByName(element.getName());

                        if (!vendorFacade.isVendorAvailable()) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format("Remote provider '%s' is not available: '%s'",
                                                    configService.getConfig().getService().getProvider().toString(),
                                                    element.getName())
                                    ));

                            awaiter.countDown();

                            return;
                        }

                        StateService.getVendorAvailability().set(true);

                        String record;

                        try {
                            record = vendorFacade.getLatestRecord(element.getName());
                        } catch (LocationDefinitionsAreNotValidException e) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Skipping retrieval of content for '%s' location: %s",
                                                    element.getName(),
                                                    e.getMessage())));

                            StateService.removeSuspenderByName(element.getName());

                            closable.countDown();

                            return;
                        } catch (GitHubContentRetrievalFailureException e) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Failed to retrieve content for '%s' location: %s",
                                                    element.getName(),
                                                    e.getMessage())));

                            awaiter.countDown();

                            return;
                        }

                        Boolean rawContentPresent = false;

                        try {
                            rawContentPresent =
                                    apiServerCommunicationResource.retrieveRawContentPresent(
                                            element.getName(), record);
                        } catch (ApiServerOperationFailureException ignored) {
                        }

                        if (!rawContentPresent) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Downloading raw content '%s' location and '%s' record",
                                                    element.getName(),
                                                    record)));

                            DataBuffer contentStream;

                            try {
                                contentStream = vendorFacade.getRecordRawContent(element.getName(), record);
                            } catch (LocationDefinitionsAreNotValidException e) {
                                logger.info(
                                        LoggingConfigurationHelper.getTransferableMessage(
                                                String.format(
                                                        "Skipping retrieval of content for '%s' location: %s",
                                                        element.getName(),
                                                        e.getMessage())));

                                StateService.removeSuspenderByName(element.getName());

                                closable.countDown();

                                return;
                            } catch (GitHubContentRetrievalFailureException e) {
                                logger.info(
                                        LoggingConfigurationHelper.getTransferableMessage(
                                                String.format(
                                                        "Failed to retrieve content for '%s' location: %s",
                                                        element.getName(),
                                                        e.getMessage())));

                                awaiter.countDown();

                                return;
                            }

                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Transferring downloaded raw content for '%s' location and '%s' record",
                                                    element.getName(),
                                                    record)));

                            try {
                                apiServerCommunicationResource.performRawContentUpload(
                                        element.getName(), record, contentStream);
                            } catch (ApiServerOperationFailureException e) {
                                logger.info(
                                        LoggingConfigurationHelper.getTransferableMessage(
                                                String.format(
                                                        "Failed to perform content uploading for '%s' location: %s",
                                                        element.getName(),
                                                        e.getMessage())));

                                awaiter.countDown();

                                return;
                            }

                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Raw content transfer finished for '%s' location and '%s' record",
                                                    element.getName(),
                                                    record)));
                        }

                        AdditionalContentDto additionalContent;

                        try {
                            additionalContent = vendorFacade.getAdditionalContent(element.getName());
                        } catch (LocationDefinitionsAreNotValidException e) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Skipping retrieval of content for '%s' location: %s",
                                                    element.getName(),
                                                    e.getMessage())));

                            StateService.removeSuspenderByName(element.getName());

                            closable.countDown();

                            return;
                        } catch (GitHubContentRetrievalFailureException e) {
                            logger.info(
                                    LoggingConfigurationHelper.getTransferableMessage(
                                            String.format(
                                                    "Failed to retrieve content for '%s' location: %s",
                                                    element.getName(),
                                                    e.getMessage())));

                            awaiter.countDown();

                            return;
                        }

                        if (Objects.nonNull(additionalContent)) {
                            Boolean additionalContentPresent = false;

                            try {
                                additionalContentPresent =
                                        apiServerCommunicationResource.retrieveAdditionalContentPresent(
                                                element.getName(), additionalContent.getHash());
                            } catch (ApiServerOperationFailureException ignored) {
                            }

                            if (!additionalContentPresent) {
                                logger.info(
                                        LoggingConfigurationHelper.getTransferableMessage(
                                                String.format(
                                                        "Transferring downloaded additional content for '%s' location and '%s' hash",
                                                        element.getName(),
                                                        additionalContent.getHash())));

                                try {
                                    apiServerCommunicationResource.performAdditionalContentUpload(
                                            element.getName(), additionalContent.getHash(), additionalContent.getData());
                                } catch (ApiServerOperationFailureException e) {
                                    logger.info(
                                            LoggingConfigurationHelper.getTransferableMessage(
                                                    String.format(
                                                            "Failed to perform additional content uploading for '%s' location: %s",
                                                            element.getName(),
                                                            e.getMessage())));

                                    awaiter.countDown();

                                    return;
                                }

                                logger.info(
                                        LoggingConfigurationHelper.getTransferableMessage(
                                                String.format(
                                                        "Additional content transfer finished for '%s' location and '%s' hash",
                                                        element.getName(),
                                                        additionalContent.getHash())));
                            }
                        }

                        awaiter.countDown();
                    } finally {
                        logger.info(
                                LoggingConfigurationHelper.getTransferableMessage(
                                        String.format("Exiting execution for '%s' location", element.getName())));
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
