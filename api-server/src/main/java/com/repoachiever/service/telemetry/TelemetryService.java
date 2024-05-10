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
     * Increases allocated clusters amount counter.
     *
     * @param value given increment value.
     */
    public void increaseClustersAmountBy(Integer value) {
        telemetryBinding.getWorkerAmount().set(telemetryBinding.getWorkerAmount().get() + value);
    }

    /**
     * Decreases allocated clusters amount counter.
     *
     * @param value given increment value.
     */
    public void decreaseClustersAmountBy(Integer value) {
        telemetryBinding.getWorkerAmount().set(telemetryBinding.getWorkerAmount().get() - value);
    }

    /**
     * Records latest cluster allocation health check.
     */
    public void recordClusterHealthCheck() {

    }
}
