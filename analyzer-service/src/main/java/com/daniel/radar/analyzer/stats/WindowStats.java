package com.daniel.radar.analyzer.stats;

import java.time.Instant;

public class WindowStats {

    private final Instant windowStart;
    private final Instant windowEnd;
    private final int sampleCount;
    private final double mean;
    private final double stdDev;
    private final double p25;
    private final double p50;
    private final double p75;
    private final double p95;
    private final double min;
    private final double max;
    private final double errorRate;
    private final double rps;

    public WindowStats(
            Instant windowStart,
            Instant windowEnd,
            int sampleCount,
            double mean,
            double stdDev,
            double p25,
            double p50,
            double p75,
            double p95,
            double min,
            double max,
            double errorRate,
            double rps
    ) {
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
        this.sampleCount = sampleCount;
        this.mean = mean;
        this.stdDev = stdDev;
        this.p25 = p25;
        this.p50 = p50;
        this.p75 = p75;
        this.p95 = p95;
        this.min = min;
        this.max = max;
        this.errorRate = errorRate;
        this.rps = rps;
    }

    public Instant getWindowStart() {
        return windowStart;
    }

    public Instant getWindowEnd() {
        return windowEnd;
    }

    public int getSampleCount() {
        return sampleCount;
    }

    public double getMean() {
        return mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public double getP25() {
        return p25;
    }

    public double getP50() {
        return p50;
    }

    public double getP75() {
        return p75;
    }

    public double getP95() {
        return p95;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getErrorRate() {
        return errorRate;
    }

    public double getRps() {
        return rps;
    }

    @Override
    public String toString() {
        return "WindowStats{" +
                "windowStart=" + windowStart +
                ", windowEnd=" + windowEnd +
                ", sampleCount=" + sampleCount +
                ", mean=" + mean +
                ", stdDev=" + stdDev +
                ", p25=" + p25 +
                ", p50=" + p50 +
                ", p75=" + p75 +
                ", p95=" + p95 +
                ", min=" + min +
                ", max=" + max +
                ", errorRate=" + errorRate +
                ", rps=" + rps +
                '}';
    }
}
