package com.daniel.radar.ingestor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class LatencyMetrics {

    private final Timer latencyTimer;

    public LatencyMetrics(MeterRegistry registry) {

        this.latencyTimer = Timer.builder("http_latency_ms")
                .description("Latencia de eventos procesados en milisegundos")

                // 🔥 Necesario para que Prometheus genere http_latency_ms_bucket{le="..."}
                .serviceLevelObjectives(
                        Duration.ofMillis(100),
                        Duration.ofMillis(300),
                        Duration.ofMillis(600),
                        Duration.ofMillis(1000),
                        Duration.ofMillis(2000),
                        Duration.ofMillis(4000)
                )

                // 🔥 Exporta histogramas completos tipo _bucket
                .publishPercentileHistogram(true)

                // Percentiles accesibles vía Prometheus
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)

                // Recomendado para diferenciar servicios
                .tag("service", "ingestor-service")

                .register(registry);
    }

    public void record(long millis) {
        latencyTimer.record(millis, TimeUnit.MILLISECONDS);
    }
}
