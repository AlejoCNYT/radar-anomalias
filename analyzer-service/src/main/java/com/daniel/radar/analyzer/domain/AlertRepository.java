package com.daniel.radar.analyzer.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AlertRepository {

    private final AlertStore store;

    public AlertRepository(AlertStore store) {
        this.store = store;
    }

    public void save(Alert alert) {
        store.add(alert);
    }

    public List<Alert> findAll() {
        return store.list();
    }

    public List<Alert> findBySeverity(Alert.Severity severity) {
        return store.list()
                .stream()
                .filter(a -> a.getSeverity() == severity)
                .collect(Collectors.toList());
    }

    public int count() {
        return store.count();
    }

    public void clear() {
        store.clear();
    }
}
