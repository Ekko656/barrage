package com.barrage.service;
import com.barrage.model.LoadTestRequest;
import com.barrage.model.LoadTestResult;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadTestService {

    // Singleton client — reused across tests, avoids thread starvation
    private HttpClient client;

    @PostConstruct
    public void init() {
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
    }

    private double percentile(List<Long> sorted, double pct) {
        if (sorted.isEmpty()) return 0;
        int idx = (int) Math.ceil(pct / 100.0 * sorted.size()) - 1;
        return sorted.get(Math.max(0, Math.min(idx, sorted.size() - 1)));
    }

    public LoadTestResult runTest(LoadTestRequest request) throws InterruptedException {
        int requestCount = Math.max(1, Math.min(request.getRequestCount(), 200));
        int concurrency  = Math.max(1, Math.min(request.getConcurrency(), requestCount));

        List<Long> responseTimes = Collections.synchronizedList(new ArrayList<>());
        Map<Integer, Integer> statusCodes = new ConcurrentHashMap<>();
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        CountDownLatch latch = new CountDownLatch(requestCount);
        long wallStart = System.currentTimeMillis();

        for (int i = 0; i < requestCount; i++) {
            executor.submit(() -> {
                try {
                    HttpRequest httpRequest = HttpRequest.newBuilder()
                            .uri(URI.create(request.getUrl()))
                            .timeout(Duration.ofSeconds(8))
                            .header("User-Agent", "Barrage/1.0")
                            .GET().build();

                    long start   = System.currentTimeMillis();
                    HttpResponse<Void> response = client.send(
                        httpRequest, HttpResponse.BodyHandlers.discarding()
                    );
                    long elapsed = System.currentTimeMillis() - start;

                    responseTimes.add(elapsed);
                    int code = response.statusCode();
                    statusCodes.merge(code, 1, Integer::sum);
                    if (code >= 200 && code < 400) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    failureCount.incrementAndGet();
                    statusCodes.merge(0, 1, Integer::sum);
                } finally {
                    latch.countDown();
                }
            });
        }

        // Hard ceiling of 30s — no test should ever hang longer than this
        boolean completed = latch.await(30, TimeUnit.SECONDS);
        executor.shutdownNow();

        if (!completed) {
            // Count any requests that never responded as failures
            long missing = requestCount - responseTimes.size() - failureCount.get();
            if (missing > 0) failureCount.addAndGet((int) missing);
        }

        long totalDuration = System.currentTimeMillis() - wallStart;
        List<Long> sorted = new ArrayList<>(responseTimes);
        Collections.sort(sorted);

        LoadTestResult result = new LoadTestResult();
        result.setUrl(request.getUrl());
        result.setTotalRequests(requestCount);
        result.setSuccessCount(successCount.get());
        result.setFailureCount(failureCount.get());
        result.setStatusCodeCounts(statusCodes);
        result.setResponseTimes(new ArrayList<>(responseTimes));
        result.setTotalDurationMs(totalDuration);

        if (!sorted.isEmpty()) {
            result.setAvgResponseTimeMs(sorted.stream().mapToLong(Long::longValue).average().orElse(0));
            result.setMinResponseTimeMs(sorted.get(0));
            result.setMaxResponseTimeMs(sorted.get(sorted.size() - 1));
            result.setP50(percentile(sorted, 50));
            result.setP90(percentile(sorted, 90));
            result.setP99(percentile(sorted, 99));
        }

        if (totalDuration > 0) {
            result.setRequestsPerSecond(
                Math.round((requestCount / (totalDuration / 1000.0)) * 10.0) / 10.0
            );
        }

        return result;
    }
}
