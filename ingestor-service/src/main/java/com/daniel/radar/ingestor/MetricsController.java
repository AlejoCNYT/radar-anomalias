package com.daniel.radar.ingestor;

import org.springframework.web.bind.annotation.*;

@RestController
public class MetricsController {

    private final LatencyProducer producer;

    public MetricsController(LatencyProducer producer){
        this.producer = producer;
    }

    @PostMapping("/simulate")
    public String simulate() {
        producer.generateRandom();
        return "OK Latency simulated";
    }
}
