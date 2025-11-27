package com.daniel.radar.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class PrometheusService {

    private static final Logger log = LoggerFactory.getLogger(PrometheusService.class);

    private final RestTemplate rest = new RestTemplate();

    public double queryP95(String service) {

        // 🔥 NUEVO nombre correcto del histograma
        String q =
                "histogram_quantile(0.95, rate(http_latency_ms_milliseconds_bucket{service=\\\""
                        + service + "\\\"}[5m]))";

        // Codificación obligatoria
        String encodedQuery = URLEncoder.encode(q, StandardCharsets.UTF_8);

        String finalUrl = "http://prometheus:9090/api/v1/query?query=" + encodedQuery;

        log.info("🔎 Ejecutando consulta a Prometheus: {}", finalUrl);

        try {
            URI uri = new URI(finalUrl);

            Map response = rest.getForObject(uri, Map.class);

            if (response == null || !response.containsKey("data")) {
                log.warn("⚠ Prometheus respondió vacío o sin 'data'");
                return -1.0;
            }

            Map dataMap = (Map) response.get("data");
            List results = (List) dataMap.get("result");

            if (results == null || results.isEmpty()) {
                log.info("ℹ Prometheus no retornó resultados para {}", service);
                return -1.0;
            }

            Map first = (Map) results.get(0);
            List value = (List) first.get("value");

            if (value == null || value.size() < 2) {
                log.warn("⚠ Formato inesperado: {}", first);
                return -1.0;
            }

            double result = Double.parseDouble((String) value.get(1));
            log.info("✔ Valor p95 obtenido: {}", result);

            return result;

        } catch (Exception e) {
            log.error("🔥 Error consultando Prometheus", e);
            return -1.0;
        }
    }
}
