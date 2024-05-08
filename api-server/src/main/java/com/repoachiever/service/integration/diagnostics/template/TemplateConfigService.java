package com.repoachiever.service.integration.diagnostics.template;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.DiagnosticsTemplateProcessingFailureException;
import com.repoachiever.service.config.ConfigService;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.*;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static freemarker.template.Configuration.VERSION_2_3_32;

/**
 * Service used to perform diagnostics template configuration operations.
 */
@Startup
@Priority(value = 160)
@ApplicationScoped
public class TemplateConfigService {
    private static final Logger logger = LogManager.getLogger(TemplateConfigService.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    /**
     * Performs diagnostics infrastructure configuration templates parsing operations.
     */
    @PostConstruct
    private void process() {
        Configuration cfg = new Configuration(VERSION_2_3_32);
        try {
            cfg.setTemplateLoader(new FileTemplateLoader(new File(properties.getDiagnosticsPrometheusConfigLocation())));
        } catch (IOException e) {
            logger.fatal(new DiagnosticsTemplateProcessingFailureException(e.getMessage()).getMessage());
            return;
        }
        cfg.setDefaultEncoding("UTF-8");

        Template template;
        try {
            template = cfg.getTemplate(properties.getDiagnosticsPrometheusConfigTemplate());
        } catch (IOException e) {
            logger.fatal(new DiagnosticsTemplateProcessingFailureException(e.getMessage()).getMessage());
            return;
        }

        Writer fileWriter;

        try {
            fileWriter = new FileWriter(
                    Paths.get(
                                    properties.getDiagnosticsPrometheusConfigLocation(),
                                    properties.getDiagnosticsPrometheusConfigOutput()).
                            toFile());
        } catch (IOException e) {
            logger.fatal(new DiagnosticsTemplateProcessingFailureException(e.getMessage()).getMessage());
            return;
        }

        // TODO: replace this with metrics endpoint point
        Map<String, Object> input = new HashMap<>() {
            {
                put("metrics", new HashMap<String, Object>() {
                    {
                        put("host", "host.docker.internal");
                        put("port", String.valueOf(configService.getConfig().getDiagnostics().getMetrics().getPort()));
                    }
                });
                put("nodeexporter", new HashMap<String, Object>() {
                    {
                        put("host", properties.getDiagnosticsPrometheusNodeExporterDockerName());
                        put("port", String.valueOf(configService.getConfig().getDiagnostics().getNodeExporter().getPort()));
                    }
                });
            }
        };

        try {
            template.process(input, fileWriter);
        } catch (TemplateException | IOException e) {
            logger.fatal(new DiagnosticsTemplateProcessingFailureException(e.getMessage()).getMessage());
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                logger.fatal(new DiagnosticsTemplateProcessingFailureException(e.getMessage()).getMessage());
            }
        }

    }
}
