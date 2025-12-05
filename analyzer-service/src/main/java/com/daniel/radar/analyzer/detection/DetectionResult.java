package com.daniel.radar.analyzer.detection;

public class DetectionResult {

    private final boolean anomaly;
    private final String method;
    private final double score;
    private final Severity severity;
    private final String reason;

    private DetectionResult(boolean anomaly, String method, double score, Severity severity, String reason) {
        this.anomaly = anomaly;
        this.method = method;
        this.score = score;
        this.severity = severity;
        this.reason = reason;
    }

    public static DetectionResult normal(String method) {
        return new DetectionResult(false, method, 0.0, Severity.NORMAL, "no anomaly");
    }

    public static DetectionResult anomaly(String method, double score, Severity severity, String reason) {
        return new DetectionResult(true, method, score, severity, reason);
    }

    public boolean isAnomaly() {
        return anomaly;
    }

    public String getMethod() {
        return method;
    }

    public double getScore() {
        return score;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "DetectionResult{" +
                "anomaly=" + anomaly +
                ", method='" + method + '\'' +
                ", score=" + score +
                ", severity=" + severity +
                ", reason='" + reason + '\'' +
                '}';
    }
}
