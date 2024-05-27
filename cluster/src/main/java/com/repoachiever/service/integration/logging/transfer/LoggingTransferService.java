package com.repoachiever.service.integration.logging.transfer;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.apiserver.resource.ApiServerCommunicationResource;
import com.repoachiever.service.state.StateService;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * Service used to handle incoming logging messages to be transferred to RepoAchiever API Server allocation.
 */
@Component
public class LoggingTransferService {
    private static final Logger logger = LogManager.getLogger(LoggingTransferService.class);

    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ApiServerCommunicationResource apiServerCommunicationResource;

    private final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    /**
     * Performs application logs transfer to RepoAchiever API Server allocation.
     */
    @PostConstruct
    private void process() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (!StateService.getExit()) {
                while (!StateService.getLogMessagesQueue().isEmpty()) {
                    try {
                        apiServerCommunicationResource.performLogsTransfer(
                                StateService.getLogMessagesQueue().poll());

                    } catch (ApiServerOperationFailureException e) {
                        logger.fatal(e.getMessage());
                    }
                }
            }
        }, 0, properties.getLoggingTransferFrequency(), TimeUnit.MILLISECONDS);
    }
}