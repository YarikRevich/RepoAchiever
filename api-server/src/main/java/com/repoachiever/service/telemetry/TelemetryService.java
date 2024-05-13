package com.repoachiever.service.telemetry;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.telemetry.binding.TelemetryBinding;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides access to gather information and expose it to telemetry representation tool.
 */
@ApplicationScoped
public class TelemetryService {
    @Inject
    PropertiesEntity properties;

    @Inject
    ConfigService configService;

    @Inject
    TelemetryBinding telemetryBinding;

    private final ConcurrentLinkedQueue<Runnable> clusterStateQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Runnable> apiServerHealthCheckQueue = new ConcurrentLinkedQueue<>();

    private final ConcurrentLinkedQueue<Runnable> clusterHealthCheckQueue = new ConcurrentLinkedQueue<>();

    private final static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

    /**
     * Starts telemetries listener, which handles incoming telemetries to be processed in a sequential way.
     */
    @PostConstruct
    private void configure() {
        executorService.scheduleAtFixedRate(() -> {
            if (!apiServerHealthCheckQueue.isEmpty()) {
                apiServerHealthCheckQueue.poll().run();
            }
        }, 0, properties.getDiagnosticsScrapeDelay(), TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            if (!clusterHealthCheckQueue.isEmpty()) {
                clusterHealthCheckQueue.poll().run();
            }
        }, 0, properties.getDiagnosticsScrapeDelay(), TimeUnit.MILLISECONDS);

        executorService.scheduleAtFixedRate(() -> {
            if (!clusterStateQueue.isEmpty()) {
                clusterStateQueue.poll().run();
            }
        }, 0, properties.getDiagnosticsScrapeDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Increases allocated workers amount counter.
     */
    public void increaseWorkersAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterStateQueue.add(() -> telemetryBinding.getWorkerAmount().set(telemetryBinding.getWorkerAmount().get() + 1));
        }
    }

    /**
     * Decreases allocated workers amount counter.
     */
    public void decreaseWorkersAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterStateQueue.add(() -> telemetryBinding.getWorkerAmount().set(telemetryBinding.getWorkerAmount().get() - 1));
        }
    }

    /**
     * Increases serving RepoAchiever Cluster allocations amount counter.
     */
    public void increaseServingClustersAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterStateQueue.add(() -> telemetryBinding.getServingClusterAmount().set(telemetryBinding.getServingClusterAmount().get() + 1));
        }
    }

    /**
     * Decreases serving RepoAchiever Cluster allocations amount counter.
     */
    public void decreaseServingClustersAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterStateQueue.add(() -> telemetryBinding.getServingClusterAmount().set(telemetryBinding.getServingClusterAmount().get() - 1));
        }
    }

    /**
     * Increases suspended RepoAchiever Cluster allocations amount counter.
     */
    public void increaseSuspendedClustersAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterStateQueue.add(() -> telemetryBinding.getSuspendedClusterAmount().set(telemetryBinding.getSuspendedClusterAmount().get() + 1));
        }
    }

    /**
     * Decreases suspended RepoAchiever Cluster allocations amount counter.
     */
    public void decreaseSuspendedClustersAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterStateQueue.add(() -> telemetryBinding.getSuspendedClusterAmount().set(telemetryBinding.getSuspendedClusterAmount().get() - 1));
        }
    }

    /**
     * Increases healthcheck requests for RepoAchiever API Server instance amount counter.
     */
    public void increaseApiServerHealthCheckAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            apiServerHealthCheckQueue.add(() -> telemetryBinding.getApiServerHealthCheckAmount().set(telemetryBinding.getApiServerHealthCheckAmount().get() + 1));
        }
    }

    /**
     * Decreases healthcheck requests for RepoAchiever API Server instance amount counter.
     */
    public void decreaseApiServerHealthCheckAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            apiServerHealthCheckQueue.add(() -> telemetryBinding.getApiServerHealthCheckAmount().set(telemetryBinding.getApiServerHealthCheckAmount().get() - 1));
        }
    }

    /**
     * Increases healthcheck requests for RepoAchiever Cluster allocations amount counter.
     */
    public void increaseClusterHealthCheckAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterHealthCheckQueue.add(() -> telemetryBinding.getClusterHealthCheckAmount().set(telemetryBinding.getClusterHealthCheckAmount().get() + 1));
        }
    }

    /**
     * Decreases healthcheck requests for RepoAchiever Cluster allocations amount counter.
     */
    public void decreaseClusterHealthCheckAmount() {
        if (configService.getConfig().getDiagnostics().getEnabled()) {
            clusterHealthCheckQueue.add(() -> telemetryBinding.getClusterHealthCheckAmount().set(telemetryBinding.getClusterHealthCheckAmount().get() - 1));
        }
    }
}
