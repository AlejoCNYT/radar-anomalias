    package com.daniel.radar.ingestor;

    import io.micrometer.core.instrument.DistributionSummary;
    import io.micrometer.core.instrument.MeterRegistry;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/events")
    public class IngestController {

        private final MeterRegistry registry;

        public IngestController(MeterRegistry registry) {
            this.registry = registry;
        }

        @PostMapping
        public ResponseEntity<String> ingest(@RequestBody Event e) {

            // 🔹 Histograma REAL con tags dinámicos (service, region)
            DistributionSummary.builder("http_latency_ms")
                    .baseUnit("milliseconds")
                    .description("Histogram of HTTP latencies in ms")
                    .publishPercentileHistogram()                 // genera *_bucket
                    .publishPercentiles(0.5, 0.9, 0.95, 0.99)
                    .tag("service", e.service)
                    .tag("region", e.region)
                    .register(registry)
                    .record(e.latency_ms);

            // 🔹 Contador de errores por status
            registry.counter("http_errors_total",
                            "service", e.service,
                            "status", String.valueOf(e.status_code))
                    .increment();

            return ResponseEntity.ok("OK");
        }
    }
