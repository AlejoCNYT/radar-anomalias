package com.daniel.radar.ingestor;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class IngestController {

    private final DistributionSummary latencyHistogram;

    public IngestController(MeterRegistry registry) {

        // HISTOGRAMA REAL COMPATIBLE CON PROMETHEUS
        this.latencyHistogram = DistributionSummary.builder("http_latency_ms")
                .baseUnit("milliseconds")
                .description("Histogram of HTTP latencies in ms")
                .publishPercentileHistogram()      // <-- CREA *_bucket
                .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                .register(registry);
    }

    @PostMapping
    public ResponseEntity<String> ingest(@RequestBody Event e) {

        // Registrar en histograma REAL
        latencyHistogram.record(e.latency_ms);

        return ResponseEntity.ok("OK");
    }
}
