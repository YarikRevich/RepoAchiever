package com.repoachiever.service.integration.diagnostics.telemetry;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.exception.ApplicationStartGuardFailureException;
import com.repoachiever.exception.TelemetryOperationFailureException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.telemetry.binding.TelemetryBinding;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service used to perform diagnostics telemetry configuration operations.
 */
@Startup(value = 500)
@ApplicationScoped
public class TelemetryConfigService {
    private static final Logger logger = LogManager.getLogger(TelemetryConfigService.class);

    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    @Inject
    TelemetryBinding telemetryBinding;

    private ServerSocket connector;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Performs telemetry metrics server configuration, registering bindings provided by external provider.
     *
     * @throws ApplicationStartGuardFailureException if RepoAchiever API Server application start guard operation fails.
     * @throws TelemetryOperationFailureException    if RepoAchiever API Server telemetry operation fails.
     */
    @PostConstruct
    private void configure() throws
            ApplicationStartGuardFailureException,
            TelemetryOperationFailureException {
        try {
            StateService.getStartGuard().await();
        } catch (InterruptedException e) {
            throw new ApplicationStartGuardFailureException(e.getMessage());
        }

        try {
            connector = new ServerSocket(
                    configService.getConfig().getDiagnostics().getMetrics().getPort());
        } catch (IOException e) {
            throw new TelemetryOperationFailureException(e.getMessage());
        }

        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        new JvmThreadMetrics().bindTo(prometheusRegistry);
        new JvmMemoryMetrics().bindTo(prometheusRegistry);
        new DiskSpaceMetrics(new File(properties.getWorkspaceDirectory())).bindTo(prometheusRegistry);
        new ProcessorMetrics().bindTo(prometheusRegistry);
        new UptimeMetrics().bindTo(prometheusRegistry);

        telemetryBinding.bindTo(prometheusRegistry);

        Thread.ofPlatform().start(() -> {
            while (!connector.isClosed()) {
                Socket connection;

                try {
                    connection = connector.accept();
                } catch (IOException ignored) {
                    continue;
                }

                try {
                    connection.setSoTimeout(properties.getDiagnosticsMetricsConnectionTimeout());
                } catch (SocketException e) {
                    logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                    return;
                }

                executorService.execute(() -> {
                    OutputStreamWriter outputStreamWriter;

                    try {
                        outputStreamWriter =
                                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                        return;
                    }

                    BufferedReader inputStreamReader;

                    try {
                        inputStreamReader = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                        return;
                    }

                    try {
                        outputStreamWriter.write(
                                String.format(
                                        "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nConnection: close\r\n\r\n%s",
                                        prometheusRegistry.scrape()));
                    } catch (IOException ignored) {
                        return;
                    }

                    try {
                        outputStreamWriter.flush();
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                        return;
                    }

                    try {
                        inputStreamReader.readLine();
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                        return;
                    }

                    try {
                        inputStreamReader.close();
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                        return;
                    }

                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());

                        return;
                    }

                    try {
                        connection.close();
                    } catch (IOException e) {
                        logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());
                    }
                });
            }
        });
    }

    @PreDestroy
    private void close() {
        try {
            connector.close();
        } catch (IOException e) {
            logger.error(new TelemetryOperationFailureException(e.getMessage()).getMessage());
        }
    }
}
