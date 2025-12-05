package com.daniel.radar.ingestor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RestController
public class LatencyController {

    private final Timer latencyMetric;

    public LatencyController(MeterRegistry registry) {
        this.latencyMetric = Timer.builder("latency_value_ms")
                .description("Latency values ingested in milliseconds")

                // Export bucket histogram
                .publishPercentileHistogram(true)

                // Export percentiles (for Prometheus & Grafana)
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)

                // SLOs (buckets) custom
                .serviceLevelObjectives(
                        Duration.ofMillis(200),
                        Duration.ofMillis(500),
                        Duration.ofMillis(1000),
                        Duration.ofMillis(2000),
                        Duration.ofMillis(4000)
                )

                // Add useful tag
                .tag("service", "ingestor-service")

                .register(registry);
    }

    @GetMapping("/simulate")
    public String simulate() {
        long value = (long) (Math.random() * 2000);

        latencyMetric.record(value, TimeUnit.MILLISECONDS);

        return "ok " + value + " ms";
    }
}
