@Service
public class PrometheusService {

    RestTemplate rest = new RestTemplate();

    public double queryP95(String service) {
        String q = """
        histogram_quantile(0.95, rate(http_latency_ms_bucket{service="%s"}[5m]))
        """.formatted(service);

        String url = "http://prometheus:9090/api/v1/query?query=" + URLEncoder.encode(q, UTF_8);

        var response = rest.getForObject(url, Map.class);
        var value = (List)((Map)((List)response.get("data")).get(0)).get("value");
        return Double.parseDouble((String) value.get(1));
    }
}
