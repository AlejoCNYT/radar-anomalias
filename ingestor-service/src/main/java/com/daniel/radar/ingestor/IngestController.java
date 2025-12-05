package com.daniel.radar.ingestor;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class IngestController {

    private final LatencyMetrics latencyMetrics;
    private final MeterRegistry registry;

    public IngestController(LatencyMetrics latencyMetrics, MeterRegistry registry) {
        this.latencyMetrics = latencyMetrics;
        this.registry = registry;
    }

    @PostMapping
    public ResponseEntity<String> ingest(@RequestBody Event e) {

        // Registrar latencia real
        latencyMetrics.record(e.latency_ms);

        // Registrar errores si hay
        registry.counter("http_errors_total",
                "service", e.service,
                "status", String.valueOf(e.status_code))
                .increment();

        return ResponseEntity.ok("OK");
    }
}
