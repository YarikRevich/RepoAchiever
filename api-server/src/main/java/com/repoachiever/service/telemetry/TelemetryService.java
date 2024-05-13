package com.repoachiever.service.telemetry;

import com.repoachiever.service.telemetry.binding.TelemetryBinding;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Provides access to gather information and expose it to telemetry representation tool.
 */
@ApplicationScoped
public class TelemetryService {
    @Inject
    TelemetryBinding telemetryBinding;

    /**
     * Increases allocated workers amount counter.
     *
     * @param value given increment value.
     */
    public void increaseWorkersAmountBy(Integer value) {
        telemetryBinding.getWorkerAmount().set(telemetryBinding.getWorkerAmount().get() + value);
    }

    /**
     * Decreases allocated workers amount counter.
     *
     * @param value given increment value.
     */
    public void decreaseWorkersAmountBy(Integer value) {
        telemetryBinding.getWorkerAmount().set(telemetryBinding.getWorkerAmount().get() - value);
    }

    /**
     * Increases serving RepoAchiever Cluster allocations amount counter.
     */
    public void increaseServingClustersAmount() {
        telemetryBinding.getServingClusterAmount().set(telemetryBinding.getServingClusterAmount().get() + 1);
    }

    /**
     * Decreases serving RepoAchiever Cluster allocations amount counter.
     */
    public void decreaseServingClustersAmount() {
        telemetryBinding.getServingClusterAmount().set(telemetryBinding.getServingClusterAmount().get() - 1);
    }

    /**
     * Increases suspended RepoAchiever Cluster allocations amount counter.
     */
    public void increaseSuspendedClustersAmount() {
        telemetryBinding.getSuspendedClusterAmount().set(telemetryBinding.getSuspendedClusterAmount().get() + 1);
    }

    /**
     * Decreases suspended RepoAchiever Cluster allocations amount counter.
     */
    public void decreaseSuspendedClustersAmount() {
        telemetryBinding.getSuspendedClusterAmount().set(telemetryBinding.getSuspendedClusterAmount().get() - 1);
    }

    /**
     * Increases healthcheck requests for RepoAchiever API Server instance amount counter.
     */
    public void increaseApiServerHealthCheckAmount() {
        telemetryBinding.getApiServerHealthCheckAmount().set(telemetryBinding.getApiServerHealthCheckAmount().get() + 1);
    }

    /**
     * Decreases healthcheck requests for RepoAchiever API Server instance amount counter.
     */
    public void decreaseApiServerHealthCheckAmount() {
        telemetryBinding.getApiServerHealthCheckAmount().set(telemetryBinding.getApiServerHealthCheckAmount().get() - 1);
    }

    /**
     * Increases healthcheck requests for RepoAchiever Cluster allocations amount counter.
     */
    public void increaseClusterHealthCheckAmount() {
        telemetryBinding.getClusterHealthCheckAmount().set(telemetryBinding.getClusterHealthCheckAmount().get() + 1);
    }

    /**
     * Decreases healthcheck requests for RepoAchiever Cluster allocations amount counter.
     */
    public void decreaseClusterHealthCheckAmount() {
        telemetryBinding.getClusterHealthCheckAmount().set(telemetryBinding.getClusterHealthCheckAmount().get() - 1);
    }
}
