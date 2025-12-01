# 📡 Radar de Latencia & Alertas — Sistema de Monitoreo Distribuido

Radar-Anomalías es un ecosistema de observabilidad que captura métricas de latencia, errores por servicio y dispara alertas en tiempo real utilizando **Prometheus + Alertmanager**, con visualización en un dashboard web en vivo.  

Incluye 3 microservicios principales:

| Servicio | Puerto | Función |
|---------|--------|----------|
| **ingestor-service** | `8081` | Recibe eventos y registra métricas Micrometer → Prometheus |
| **analyzer-service** | `8082` | Consulta Prometheus y expone alertas vía REST/WebSocket |
| **dashboard** | `8080` | UI que grafica P95, errores/min y alertas activas |

---

## 🛑 Arquitectura

```
CLIENTE  →  POST /events  →  INGESTOR
                 ↓ 
           /actuator/prometheus
                 ↓ scrape
           PROMETHEUS  ← reglas Alertmanager
                 ↓
ANALYZER  ← consulta API    →  /alerts   → Dashboard UI
```

---

## 📍 Tecnologías

| Componente | Tecnología |
|----------|------------|
| Backend | Spring Boot + WebSocket STOMP |
| Métricas | Micrometer + Prometheus Registry |
| Alertas | Prometheus Rules + Alertmanager |
| Visualización | HTML + JS + Chart.js |
| Infraestructura | Docker Compose |

---

## 🚀 Cómo Ejecutarlo

```bash
git clone <repo>
cd radar-anomalias
docker compose up -d --build
```

📌 Accesos:

| UI / Servicios | URL |
|---|---|
| Dashboard UI | http://localhost:8080 |
| API Analyzer JSON | http://localhost:8082/alerts |
| Prometheus Console | http://localhost:9090 |
| Alertmanager UI | http://localhost:9093 |

---

## 📤 Enviar tráfico de prueba

```powershell
for ($i=1; $i -le 120; $i++) {
    Invoke-RestMethod http://localhost:8081/events `
    -Method POST `
    -Headers @{ "Content-Type"="application/json" } `
    -Body '{ "service":"payment-api", "region":"us-east", "status_code":500, "latency_ms":4500 }'
}
```

---

## ⚠ Regla de Alerta P95

```yaml
groups:
  - name: latency-alerts
    rules:
      - alert: HighLatencyP95
        expr: histogram_quantile(
                0.95,
                rate(http_latency_ms_bucket{service="payment-api"}[5m])
              ) > 2000
        for: 1m
        labels:
          severity: warning
        annotations:
          description: "P95 supera 2000ms durante 1 minuto"
```

---

## 🖥 Dashboard Incluye

| Métrica | Estado |
|---|---|
| P95 Latency realtime | 🟢 |
| Errores por minuto | 🟢 |
| Alertas activas | 🔥 Si regla se cumple |
| WebSocket Live Feed | 🟡 Ajustable |

---

## 👤 Autor

**Daniel Alejandro Acero Varela**  
AREP · Universidad Escuela Colombiana de Ingeniería
