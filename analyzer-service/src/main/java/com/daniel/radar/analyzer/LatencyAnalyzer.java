package com.daniel.radar.analyzer;

import com.daniel.radar.analyzer.domain.Alert;
import com.daniel.radar.analyzer.domain.AlertStore;
import com.daniel.radar.analyzer.detection.*;
import com.daniel.radar.analyzer.stats.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class LatencyAnalyzer {

    private final AlertStore alertStore;
    private final AlertNotifier notifier;

    private final SlidingWindowAggregator window =
            new SlidingWindowAggregator(Duration.ofMinutes(5));

    private final EnsembleDetector detector =
            new EnsembleDetector(
                    new ZScoreDetector(3.0),
                    new IqrDetector(1.5)
            );

    private static final double HARD_SLA_MS = 4000.0;

    public LatencyAnalyzer(AlertStore alertStore, AlertNotifier notifier) {
        this.alertStore = alertStore;
        this.notifier = notifier;
    }

    public void onNewSample(double latencyMs, boolean error) {
        window.addSample(latencyMs, error, Instant.now());
    }

    @Scheduled(fixedRate = 3000)
    public void analyzeWindow() {

        WindowStats stats = window.computeStats();

        if (stats.getSampleCount() < 15) {
            System.out.println("⚠️ No hay suficientes datos para analizar (" +
                    stats.getSampleCount() + " muestras)");
            return;
        }

        double latestValue = stats.getP95();

        if (latestValue > HARD_SLA_MS) {

            Alert alert = new Alert(
                    "payment-api",
                    "latency_ms_p95",
                    latestValue,
                    HARD_SLA_MS,
                    "hard-threshold-4s",
                    Alert.Severity.CRITICAL
            );

            alertStore.add(alert);
            notifier.notify(alert);

            System.out.println("🚨 ALERTA (HARD SLA) GENERADA: " + alert);
            return;
        }

        DetectionResult result = detector.detect(latestValue, stats);

        if (!result.isAnomaly()) {
            System.out.println("👌 Normal | mean=%.2f p95=%.2f".formatted(
                    stats.getMean(), stats.getP95()));
            return;
        }

        Alert alert = new Alert(
                "payment-api",
                "latency_ms_p95",
                latestValue,
                stats.getP95(),
                result.getMethod(),
                convertSeverity(result.getSeverity())
        );

        alertStore.add(alert);
        notifier.notify(alert);

        System.out.println("🚨 ALERTA GENERADA: " + alert);
        System.out.println("   ↳ " + result.getReason());
    }

    private Alert.Severity convertSeverity(Severity sev) {
        return switch (sev) {
            case CRITICAL -> Alert.Severity.CRITICAL;
            case MAJOR    -> Alert.Severity.WARNING;
            case MINOR    -> Alert.Severity.WARNING;
            default       -> Alert.Severity.INFO;
        };
    }
}
