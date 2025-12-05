package com.daniel.radar.analyzer.domain;

import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class AlertStore {

    private final LinkedList<Alert> alerts = new LinkedList<>();

    public synchronized void add(Alert alert) {
        alerts.addFirst(alert);

        if (alerts.size() > 500) {
            alerts.removeLast();
        }
    }

    public synchronized List<Alert> list() {
        return new LinkedList<>(alerts);
    }

    public synchronized void clear() {
        alerts.clear();
    }

    public synchronized int count() {
        return alerts.size();
    }
}
