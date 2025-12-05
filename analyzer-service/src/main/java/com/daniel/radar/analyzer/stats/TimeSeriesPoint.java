package com.daniel.radar.analyzer.stats;

import java.time.Instant;

public class TimeSeriesPoint {

    private final Instant timestamp;
    private final double value;
    private final boolean error;

    public TimeSeriesPoint(Instant timestamp, double value, boolean error) {
        this.timestamp = timestamp;
        this.value = value;
        this.error = error;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public double getValue() {
        return value;
    }

    public boolean isError() {
        return error;
    }
}
