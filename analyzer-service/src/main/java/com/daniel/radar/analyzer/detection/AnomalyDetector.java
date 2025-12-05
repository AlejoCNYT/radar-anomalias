package com.daniel.radar.analyzer.detection;

import com.daniel.radar.analyzer.stats.WindowStats;

public interface AnomalyDetector {

    /**
     * Detecta anomalías usando las estadísticas de la ventana.
     * @param latestValue valor más reciente de la métrica (ej. latencia p95 o último sample)
     * @param stats estadísticas de la ventana deslizante
     */
    DetectionResult detect(double latestValue, WindowStats stats);
}
