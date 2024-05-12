package com.repoachiever.service.integration.diagnostics.template;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ApplicationStartGuardFailureException;
import com.repoachiever.exception.DiagnosticsTemplateProcessingFailureException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static freemarker.template.Configuration.VERSION_2_3_32;

/**
 * Service used to perform diagnostics template configuration operations.
 */
@Startup(value = 600)
@ApplicationScoped
public class TemplateConfigService {
    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    /**
     * Performs diagnostics infrastructure configuration templates parsing operations.
     *
     * @throws ApplicationStartGuardFailureException         if RepoAchiever API Server application start guard operation fails.
     * @throws DiagnosticsTemplateProcessingFailureException if RepoAchiever API Server diagnostics template processing
     *                                                       operation fails.
     */
    @PostConstruct
    private void process() throws
            ApplicationStartGuardFailureException,
            DiagnosticsTemplateProcessingFailureException {
        try {
            StateService.getStartGuard().await();
        } catch (InterruptedException e) {
            throw new ApplicationStartGuardFailureException(e.getMessage());
        }

        if (configService.getConfig().getDiagnostics().getEnabled()) {
            Configuration cfg = new Configuration(VERSION_2_3_32);
            try {
                cfg.setTemplateLoader(new FileTemplateLoader(new File(properties.getDiagnosticsPrometheusConfigLocation())));
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }
            cfg.setDefaultEncoding("UTF-8");

            Template template;

            try {
                template = cfg.getTemplate(properties.getDiagnosticsPrometheusConfigTemplate());
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            Writer fileWriter;

            try {
                fileWriter = new FileWriter(
                        Paths.get(
                                        properties.getDiagnosticsPrometheusConfigLocation(),
                                        properties.getDiagnosticsPrometheusConfigOutput()).
                                toFile());
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

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
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());

            } finally {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }

            try {
                cfg.setTemplateLoader(new FileTemplateLoader(
                        new File(properties.getDiagnosticsGrafanaDatasourcesLocation())));
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            try {
                template = cfg.getTemplate(properties.getDiagnosticsGrafanaDatasourcesTemplate());
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            try {
                fileWriter = new FileWriter(
                        Paths.get(
                                        properties.getDiagnosticsGrafanaDatasourcesLocation(),
                                        properties.getDiagnosticsGrafanaDatasourcesOutput()).
                                toFile());
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            input = new HashMap<>() {
                {
                    put("prometheus", new HashMap<String, Object>() {
                        {
                            put("host", properties.getDiagnosticsPrometheusDockerName());
                            put("port", String.valueOf(configService.getConfig().getDiagnostics().getPrometheus().getPort()));
                        }
                    });
                }
            };

            try {
                template.process(input, fileWriter);
            } catch (TemplateException | IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }

            try {
                cfg.setTemplateLoader(new FileTemplateLoader(
                        new File(properties.getDiagnosticsGrafanaDashboardsLocation())));
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            try {
                template = cfg.getTemplate(properties.getDiagnosticsGrafanaDashboardsDiagnosticsTemplate());
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            try {
                fileWriter = new FileWriter(
                        Paths.get(
                                        properties.getDiagnosticsGrafanaDashboardsLocation(),
                                        properties.getDiagnosticsGrafanaDashboardsDiagnosticsOutput()).
                                toFile());
            } catch (IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            }

            input = new HashMap<>() {
                {
                    put("info", new HashMap<String, Object>() {
                        {
                            put("version", properties.getGitCommitId());
                        }
                    });
                    put("nodeexporter", new HashMap<String, Object>() {
                        {
                            put("port", String.valueOf(
                                    configService.getConfig().getDiagnostics().getNodeExporter().getPort()));
                        }
                    });
                }
            };

            try {
                template.process(input, fileWriter);
            } catch (TemplateException | IOException e) {
                throw new DiagnosticsTemplateProcessingFailureException(e.getMessage());
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
