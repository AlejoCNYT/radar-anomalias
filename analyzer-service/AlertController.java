@RestController
public class AlertController {

    @Autowired PrometheusService prom;
    @Autowired ZScoreDetector detector;

    @GetMapping("/alerts")
    public List<Alert> alerts() {
        double p95 = prom.queryP95("payment-api");
        return detector.evaluate("payment-api", p95)
                .map(List::of)
                .orElse(List.of());
    }
}
