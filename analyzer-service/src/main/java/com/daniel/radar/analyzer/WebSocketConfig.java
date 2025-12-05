package com.daniel.radar.analyzer;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // Endpoint donde los clientes se conectan
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")   // permitir front en localhost (dashboard)
                .withSockJS();                   // fallback para navegadores antiguos
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Prefix de los topics que el SERVIDOR publica (broadcast)
        registry.enableSimpleBroker("/topic");

        // Prefix de los destinos que el CLIENTE envía mensajes al servidor
        registry.setApplicationDestinationPrefixes("/app");
    }
}
