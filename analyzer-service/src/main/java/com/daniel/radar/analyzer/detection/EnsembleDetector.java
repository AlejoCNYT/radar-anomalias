package com.daniel.radar.analyzer.detection;

import com.daniel.radar.analyzer.stats.WindowStats;

import java.util.ArrayList;
import java.util.List;

public class EnsembleDetector implements AnomalyDetector {

    private final List<AnomalyDetector> detectors = new ArrayList<>();

    public EnsembleDetector(AnomalyDetector... detectors) {
        for (AnomalyDetector d : detectors) {
            this.detectors.add(d);
        }
    }

    @Override
    public DetectionResult detect(double latestValue, WindowStats stats) {
        boolean anyAnomaly = false;
        boolean strongAgreement = true;
        Severity maxSeverity = Severity.NORMAL;
        StringBuilder reasons = new StringBuilder();

        for (AnomalyDetector detector : detectors) {
            DetectionResult r = detector.detect(latestValue, stats);
            if (reasons.length() > 0) {
                reasons.append(" | ");
            }
            reasons.append(r.getMethod()).append(": ").append(r.getReason());

            if (r.isAnomaly()) {
                anyAnomaly = true;
                if (r.getSeverity().ordinal() > maxSeverity.ordinal()) {
                    maxSeverity = r.getSeverity();
                }
            } else {
                strongAgreement = false;
            }
        }

        if (!anyAnomaly) {
            return DetectionResult.normal("ensemble");
        }

        // Estrategia simple:
        // - si todos detectan -> subir severidad un nivel
        // - si solo algunos detectan -> usar maxSeverity
        if (strongAgreement && maxSeverity != Severity.CRITICAL) {
            maxSeverity = Severity.values()[maxSeverity.ordinal() + 1];
        }

        return DetectionResult.anomaly(
                "ensemble",
                maxSeverity.ordinal(), // score simplificado
                maxSeverity,
                reasons.toString()
        );
    }
}
