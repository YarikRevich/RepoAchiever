package com.repoachiever;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.apiserver.resource.ApiServerCommunicationResource;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.communication.cluster.ClusterCommunicationConfigService;
import com.repoachiever.service.integration.logging.state.LoggingStateService;
import com.repoachiever.service.executor.CommandExecutorService;
import com.repoachiever.service.integration.logging.transfer.LoggingTransferService;
import com.repoachiever.service.waiter.WaiterHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

/**
 * Represents initialization point for the RepoAchiever Cluster application.
 */
@Component
@Import({
        ConfigService.class,
        CommandExecutorService.class,
        PropertiesEntity.class,
        ClusterCommunicationConfigService.class,
        ApiServerCommunicationResource.class,
        LoggingStateService.class,
        LoggingTransferService.class
})
public class App implements ApplicationRunner {
    /**
     * @see ApplicationRunner
     */
    @Override
    public void run(ApplicationArguments args) {
        WaiterHelper.waitForExit();
    }
}
