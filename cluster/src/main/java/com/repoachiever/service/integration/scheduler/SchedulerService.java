package com.repoachiever.service.integration.scheduler;

import com.repoachiever.service.config.ConfigService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service used to perform RepoAchiever Cluster worker schedule operations.
 */
@Component
public class SchedulerService {
    @Autowired
    private ConfigService configService;

    /**
     * Performs startup of RepoAchiever Cluster workers.
     */
    @PostConstruct
    private void process() {
//        configService.getConfig().getService().get
    }
}
