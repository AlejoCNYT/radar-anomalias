package com.daniel.radar.analyzer;

import com.daniel.radar.analyzer.domain.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlertNotifier {

    private final SimpMessagingTemplate websocket;

    @Autowired
    public AlertNotifier(SimpMessagingTemplate websocket) {
        this.websocket = websocket;
    }

    public void notify(Alert alert) {
        websocket.convertAndSend("/topic/alerts", alert);
    }
}
