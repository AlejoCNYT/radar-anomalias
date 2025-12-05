package com.daniel.radar.analyzer;

import com.daniel.radar.analyzer.domain.Alert;
import com.daniel.radar.analyzer.domain.AlertStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AlertController {

    private final AlertStore store;

    public AlertController(AlertStore store) {
        this.store = store;
    }

    @GetMapping("/alerts")
    public List<Alert> getAlerts() {
        return store.list();
    }

    @GetMapping("/alerts/count")
    public int count() {
        return store.count();
    }

    @DeleteMapping("/alerts")
    public void clear() {
        store.clear();
    }
}
