package com.daniel.radar.analyzer.domain;

public class AlertSummaryDTO {

    private String metric;
    private double value;
    private Alert.Severity severity;
    private String timestamp;

    public AlertSummaryDTO(String metric, double value, Alert.Severity severity, String timestamp) {
        this.metric = metric;
        this.value = value;
        this.severity = severity;
        this.timestamp = timestamp;
    }

    public static AlertSummaryDTO from(Alert alert) {
        return new AlertSummaryDTO(
                alert.getMetric(),
                alert.getValue(),
                alert.getSeverity(),
                alert.getTimestamp()
        );
    }

    // Getters

    public String getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    public Alert.Severity getSeverity() {
        return severity;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
