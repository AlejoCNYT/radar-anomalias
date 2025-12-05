package com.daniel.radar.analyzer.detection;

import com.daniel.radar.analyzer.stats.WindowStats;

public class ZScoreDetector implements AnomalyDetector {

    private final double sigmaThreshold;
    private final double stdDevEpsilon;

    public ZScoreDetector(double sigmaThreshold) {
        this(sigmaThreshold, 1e-6);
    }

    public ZScoreDetector(double sigmaThreshold, double stdDevEpsilon) {
        this.sigmaThreshold = sigmaThreshold;
        this.stdDevEpsilon = stdDevEpsilon;
    }

    @Override
    public DetectionResult detect(double latestValue, WindowStats stats) {
        if (stats.getSampleCount() < 5) {
            return DetectionResult.normal("zscore");
        }

        double std = stats.getStdDev();
        if (std < stdDevEpsilon) {
            return DetectionResult.normal("zscore");
        }

        double z = (latestValue - stats.getMean()) / std;
        double absZ = Math.abs(z);

        if (absZ <= sigmaThreshold) {
            return DetectionResult.normal("zscore");
        }

        Severity severity;
        if (absZ >= sigmaThreshold + 2) {
            severity = Severity.CRITICAL;
        } else if (absZ >= sigmaThreshold + 1) {
            severity = Severity.MAJOR;
        } else {
            severity = Severity.MINOR;
        }

        String reason = String.format("z=%.2f, value=%.2f, mean=%.2f, std=%.2f",
                z, latestValue, stats.getMean(), stats.getStdDev());

        return DetectionResult.anomaly("zscore", z, severity, reason);
    }
}
    