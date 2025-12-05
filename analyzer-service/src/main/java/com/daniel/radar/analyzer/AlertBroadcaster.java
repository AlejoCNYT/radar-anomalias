package com.daniel.radar.analyzer;

import com.daniel.radar.analyzer.domain.Alert;
import com.daniel.radar.analyzer.domain.AlertStore;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertBroadcaster {

    private final SimpMessagingTemplate ws;
    private final AlertStore store;

    public AlertBroadcaster(SimpMessagingTemplate ws, AlertStore store) {
        this.ws = ws;
        this.store = store;
    }

    /**
     * Guarda la alerta en memoria y la envía por WebSocket
     * al topic /topic/alerts (lo que consume tu dashboard).
     */
    public void send(Alert alert) {
        // Persistimos en el store in-memory
        store.add(alert);

        // Enviamos al dashboard por STOMP
        ws.convertAndSend("/topic/alerts", alert);

        System.out.println("📡 ALERTA GUARDADA/ENVIADA => " + alert);
    }
}
