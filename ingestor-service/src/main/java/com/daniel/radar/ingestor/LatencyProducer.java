package com.daniel.radar.ingestor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class LatencyProducer {

    private final RestTemplate rest = new RestTemplate();
    private final Random random = new Random();

    // Cambia si el analyzer está en otro host
    private static final String ANALYZER_URL = "http://localhost:8080/sample";

    public void generateRandom() {

        double latency = 300 + random.nextDouble()*2000;
        boolean error = random.nextDouble() < 0.2;

        String url = ANALYZER_URL +
                "?latency=" + latency +
                "&error=" + error;

        try {
            rest.postForEntity(url, null, String.class);
        } catch (Exception ex){
            System.out.println("❌ Error enviando a analyzer: " + ex.getMessage());
        }

        System.out.println("📤 Sent sample: latency="+latency+", error="+error);
    }
}
