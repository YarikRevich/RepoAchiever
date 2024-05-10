package com.repoachiever.service.telemetry.binding;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service used to create custom telemetry bindings used to distribute application metrics.
 */
@ApplicationScoped
public class TelemetryBinding implements MeterBinder {
    @Getter
    private final AtomicInteger workerAmount = new AtomicInteger();

    @Getter
    private final AtomicInteger clusterAmount = new AtomicInteger();

    @Getter
    private Timer clusterHealthCheck;

    /**
     * @see MeterBinder
     */
    @Override
    public void bindTo(@NotNull MeterRegistry meterRegistry) {
        Gauge.builder("general.worker_amount", workerAmount, AtomicInteger::get)
                .description("Represents amount of allocated workers")
                .register(meterRegistry);

        Gauge.builder("general.cluster_amount", clusterAmount, AtomicInteger::get)
                .description("Represents amount of allocated clusters")
                .register(meterRegistry);

        clusterHealthCheck = Timer.builder("general.cluster_health_check")
                .description("Represents all the performed health check requests for allocated clusters")
                .register(meterRegistry);
    }
}
