package com.daniel.radar.ingestor;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class MetricsController {

    private final Timer latencyTimer;
    private final Random random = new Random();

    public MetricsController(MeterRegistry registry) {
        this.latencyTimer = Timer.builder("http_latency_ms")
                .publishPercentileHistogram()
                .tag("service", "payment-api")
                .register(registry);
    }

    @GetMapping("/simulate")
    public String simulate() {
        latencyTimer.record(() -> {
            try {
                Thread.sleep(50 + random.nextInt(300)); // genera latencias realistas
            } catch (InterruptedException ignored) {}
        });
        return "ok";
    }
}
