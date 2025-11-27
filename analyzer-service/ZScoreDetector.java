@Service
public class ZScoreDetector {

    private final Map<String, List<Double>> history = new ConcurrentHashMap<>();

    public Optional<Alert> evaluate(String service, double value) {

        history.putIfAbsent(service, new ArrayList<>());
        var list = history.get(service);

        list.add(value);
        if (list.size() < 5) return Optional.empty();

        double mean = list.stream().mapToDouble(d -> d).average().orElse(0);
        double std = Math.sqrt(list.stream().mapToDouble(d -> Math.pow(d - mean, 2)).sum() / list.size());

        if (std == 0) return Optional.empty();

        double z = Math.abs((value - mean) / std);

        if (z >= 3) {
            return Optional.of(new Alert(service, "latency_p95", value, mean + 3*std, "zscore"));
        }

        return Optional.empty();
    }
}
