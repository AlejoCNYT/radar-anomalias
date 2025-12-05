package com.daniel.radar.analyzer;

import org.springframework.web.bind.annotation.*;

@RestController
public class SampleController {

    private final LatencyAnalyzer analyzer;

    public SampleController(LatencyAnalyzer analyzer){
        this.analyzer = analyzer;
    }

    @PostMapping("/sample")
    public void post(@RequestParam double latency,
                     @RequestParam boolean error){
        analyzer.onNewSample(latency, error);
    }
}
