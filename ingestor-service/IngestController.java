@RestController
@RequestMapping("/events")
public class IngestController {

    private final MeterRegistry registry;

    public IngestController(MeterRegistry registry) {
        this.registry = registry;
    }

    @PostMapping
    public ResponseEntity<String> ingest(@RequestBody Event e) {

        registry.timer("http_latency_ms",
                "service", e.service,
                "region", e.region)
                .record(e.latency_ms, TimeUnit.MILLISECONDS);

        registry.counter("http_errors_total",
                "service", e.service,
                "status", String.valueOf(e.status_code))
                .increment();

        return ResponseEntity.ok("OK");
    }
}
