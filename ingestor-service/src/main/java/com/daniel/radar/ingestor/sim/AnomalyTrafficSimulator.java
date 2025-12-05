    package com.daniel.radar.ingestor.sim;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Simulador simple de tráfico con anomalías.
 * Se ejecuta como clase independiente (tiene main).
 */
public class AnomalyTrafficSimulator {

    private static final String INGEST_URL = "http://localhost:8081/events";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    public static void main(String[] args) throws Exception {
        AnomalyTrafficSimulator sim = new AnomalyTrafficSimulator();

        // Ejemplos: descomenta según lo que quieras probar
        // sim.normalTraffic(2000, 100, 120);
        // sim.latencySpike(2000, 100, 120, 600, 800, 0.1);
        // sim.errorBurst(2000, 120, 0.05);
        // sim.drift(2000, 80, 300);

        sim.latencySpike(1000, 90, 120, 500, 800, 0.15);
    }

    public void normalTraffic(int requests, double minLatency, double maxLatency) throws InterruptedException {
        for (int i = 0; i < requests; i++) {
            double latency = random(minLatency, maxLatency);
            sendEvent(latency, 200);
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }

    public void latencySpike(int requests,
                             double baseMin, double baseMax,
                             double spikeMin, double spikeMax,
                             double spikeProbability) throws InterruptedException {

        for (int i = 0; i < requests; i++) {
            double latency;
            if (random.nextDouble() < spikeProbability) {
                latency = random(spikeMin, spikeMax);
            } else {
                latency = random(baseMin, baseMax);
            }
            sendEvent(latency, 200);
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }

    public void errorBurst(int requests,
                           double baseLatency,
                           double errorProbability) throws InterruptedException {
        for (int i = 0; i < requests; i++) {
            boolean error = random.nextDouble() < errorProbability;
            int status = error ? 500 : 200;
            double latency = baseLatency + random(-10, 10);
            sendEvent(latency, status);
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }

    public void drift(int requests, double startLatency, double endLatency) throws InterruptedException {
        double step = (endLatency - startLatency) / Math.max(requests - 1, 1);
        double current = startLatency;
        for (int i = 0; i < requests; i++) {
            sendEvent(current, 200);
            current += step;
            TimeUnit.MILLISECONDS.sleep(50);
        }
    }

    private void sendEvent(double latencyMs, int statusCode) {
        String payload = String.format(
                "{\"service\":\"demo-api\",\"endpoint\":\"/v1/test\",\"latency_ms\":%.2f,\"status_code\":%d}",
                latencyMs, statusCode
        );
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(INGEST_URL, payload, String.class);
            // Opcional: imprimir respuesta
            // System.out.println(response.getStatusCode());
        } catch (Exception e) {
            System.err.println("Error enviando evento: " + e.getMessage());
        }
    }

    private double random(double min, double max) {
        return min + (max - min) * random.nextDouble();
    }
}
