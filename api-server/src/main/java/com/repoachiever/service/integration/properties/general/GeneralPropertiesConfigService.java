package com.repoachiever.service.integration.properties.general;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service used to perform general properties configuration operations.
 */
@Startup
@ApplicationScoped
public class GeneralPropertiesConfigService {
    @PostConstruct
    private void process() {

    }
}