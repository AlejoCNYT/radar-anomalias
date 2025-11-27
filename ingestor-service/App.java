private final Random random = new Random();

@GetMapping("/simulate")
public String simulate() {
    // Simula latencias en buckets
    for (int i = 0; i < 10; i++) {
        double latency = 50 + random.nextInt(300); // 50-350 ms
        System.out.println("Simulated latency: " + latency);
    }
    return "ok";
}
