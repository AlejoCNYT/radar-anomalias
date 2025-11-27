package com.daniel.radar.ingestor;

public class Event {

    public String service;
    public String region;
    public long latency_ms;
    public int status_code;

    // NECESARIO PARA @RequestBody
    public Event() { }

    public Event(String service, String region, long latency_ms, int status_code) {
        this.service = service;
        this.region = region;
        this.latency_ms = latency_ms;
        this.status_code = status_code;
    }
}
