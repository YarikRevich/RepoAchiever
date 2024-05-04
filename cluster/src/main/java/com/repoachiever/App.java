package com.repoachiever;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.integration.communication.exporter.ProviderCommunicationExporterService;
import com.repoachiever.service.integration.communication.provider.ProviderCommunicationConfigService;
import com.repoachiever.service.integration.logging.LoggingStateConfigService;
import com.repoachiever.service.executor.CommandExecutorService;
import com.repoachiever.service.waiter.WaiterHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.rmi.RMISecurityManager;

/**
 * Represents initialization point for the RepoAchiever Cluster application.
 */
@Component
@Import({
        ConfigService.class,
        CommandExecutorService.class,
        PropertiesEntity.class,
        ProviderCommunicationExporterService.class,
        ProviderCommunicationConfigService.class,
        LoggingStateConfigService.class
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
