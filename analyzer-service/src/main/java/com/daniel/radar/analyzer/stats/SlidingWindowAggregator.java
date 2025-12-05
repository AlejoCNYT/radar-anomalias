package com.daniel.radar.analyzer.stats;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SlidingWindowAggregator {

    private final Duration windowSize;
    private final Deque<TimeSeriesPoint> window = new ArrayDeque<>();

    public SlidingWindowAggregator(Duration windowSize) {
        this.windowSize = windowSize;
    }

    public void addSample(double value, boolean error, Instant timestamp) {
        TimeSeriesPoint point = new TimeSeriesPoint(timestamp, value, error);
        window.addLast(point);
        evictOldSamples(timestamp);
    }

    private void evictOldSamples(Instant now) {
        Instant cutoff = now.minus(windowSize);
        while (!window.isEmpty() && window.peekFirst().getTimestamp().isBefore(cutoff)) {
            window.removeFirst();
        }
    }

    public WindowStats computeStats() {
        if (window.isEmpty()) {
            Instant now = Instant.now();
            return new WindowStats(
                    now, now, 0,
                    0, 0,
                    0, 0, 0, 0,
                    0, 0,
                    0, 0
            );
        }

        List<Double> values = new ArrayList<>(window.size());
        int errors = 0;
        double sum = 0;
        double sumSquares = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (TimeSeriesPoint p : window) {
            double v = p.getValue();
            values.add(v);
            sum += v;
            sumSquares += v * v;
            min = Math.min(min, v);
            max = Math.max(max, v);
            if (p.isError()) {
                errors++;
            }
        }

        int n = values.size();
        double mean = sum / n;
        double variance = (sumSquares / n) - (mean * mean);
        double stdDev = variance > 0 ? Math.sqrt(variance) : 0.0;

        values.sort(Double::compareTo);

        double p25 = percentile(values, 25);
        double p50 = percentile(values, 50);
        double p75 = percentile(values, 75);
        double p95 = percentile(values, 95);

        Instant start = window.peekFirst().getTimestamp();
        Instant end = window.peekLast().getTimestamp();
        double seconds = Math.max(Duration.between(start, end).toMillis() / 1000.0, 1.0);

        double errorRate = (double) errors / n;
        double rps = n / seconds;

        return new WindowStats(
                start, end, n,
                mean, stdDev,
                p25, p50, p75, p95,
                min, max,
                errorRate, rps
        );
    }

    private static double percentile(List<Double> sortedValues, int p) {
        if (sortedValues.isEmpty()) return 0.0;
        if (p <= 0) return sortedValues.get(0);
        if (p >= 100) return sortedValues.get(sortedValues.size() - 1);

        double rank = (p / 100.0) * (sortedValues.size() - 1);
        int low = (int) Math.floor(rank);
        int high = (int) Math.ceil(rank);
        if (low == high) {
            return sortedValues.get(low);
        }
        double weight = rank - low;
        return sortedValues.get(low) * (1 - weight) + sortedValues.get(high) * weight;
    }
}
