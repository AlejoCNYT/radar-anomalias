package com.daniel.radar.analyzer.detection;

import com.daniel.radar.analyzer.stats.WindowStats;

public class IqrDetector implements AnomalyDetector {

    private final double multiplier;

    public IqrDetector(double multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public DetectionResult detect(double latestValue, WindowStats stats) {
        if (stats.getSampleCount() < 5) {
            return DetectionResult.normal("iqr");
        }

        double q1 = stats.getP25();
        double q3 = stats.getP75();
        double iqr = q3 - q1;
        if (iqr <= 0) {
            return DetectionResult.normal("iqr");
        }

        double lower = q1 - multiplier * iqr;
        double upper = q3 + multiplier * iqr;

        if (latestValue >= lower && latestValue <= upper) {
            return DetectionResult.normal("iqr");
        }

        double score; // qué tan lejos está
        Severity severity;
        if (latestValue > upper) {
            score = (latestValue - upper) / iqr;
        } else {
            score = (lower - latestValue) / iqr;
        }

        if (score >= 3) {
            severity = Severity.CRITICAL;
        } else if (score >= 2) {
            severity = Severity.MAJOR;
        } else {
            severity = Severity.MINOR;
        }

        String reason = String.format("IQR=%.2f, q1=%.2f, q3=%.2f, value=%.2f, score=%.2f",
                iqr, q1, q3, latestValue, score);

        return DetectionResult.anomaly("iqr", score, severity, reason);
    }
}
