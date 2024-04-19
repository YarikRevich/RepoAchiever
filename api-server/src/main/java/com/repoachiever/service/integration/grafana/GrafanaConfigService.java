package com.repoachiever.service.integration.grafana;

import com.repoachiever.service.config.ConfigService;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service used to perform Grafana configuration operations.
 */
@Startup
@ApplicationScoped
public class GrafanaConfigService {
    private static final Logger logger = LogManager.getLogger(GrafanaConfigService.class);

    @Inject
    ConfigService configService;

    /**
     * Starts Grafana instance and exports pre-defined Grafana dashboard configurations to Grafana instance using
     * external API.
     */
    @PostConstruct
    private void process() {
        if (configService.getConfig().getOptions().getDiagnostics().getEnabled()) {

        }
    }
}
