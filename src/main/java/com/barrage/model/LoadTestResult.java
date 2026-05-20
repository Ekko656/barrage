package com.barrage.model;
import java.util.List;
import java.util.Map;

public class LoadTestResult {
    private String url;
    private int totalRequests;
    private int successCount;
    private int failureCount;
    private double avgResponseTimeMs;
    private double minResponseTimeMs;
    private double maxResponseTimeMs;
    private double p50;
    private double p90;
    private double p99;
    private double requestsPerSecond;
    private long totalDurationMs;
    private Map<Integer, Integer> statusCodeCounts;
    private List<Long> responseTimes;

    public String getUrl() { return url; }
    public void setUrl(String v) { this.url = v; }
    public int getTotalRequests() { return totalRequests; }
    public void setTotalRequests(int v) { this.totalRequests = v; }
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int v) { this.successCount = v; }
    public int getFailureCount() { return failureCount; }
    public void setFailureCount(int v) { this.failureCount = v; }
    public double getAvgResponseTimeMs() { return avgResponseTimeMs; }
    public void setAvgResponseTimeMs(double v) { this.avgResponseTimeMs = v; }
    public double getMinResponseTimeMs() { return minResponseTimeMs; }
    public void setMinResponseTimeMs(double v) { this.minResponseTimeMs = v; }
    public double getMaxResponseTimeMs() { return maxResponseTimeMs; }
    public void setMaxResponseTimeMs(double v) { this.maxResponseTimeMs = v; }
    public double getP50() { return p50; }
    public void setP50(double v) { this.p50 = v; }
    public double getP90() { return p90; }
    public void setP90(double v) { this.p90 = v; }
    public double getP99() { return p99; }
    public void setP99(double v) { this.p99 = v; }
    public double getRequestsPerSecond() { return requestsPerSecond; }
    public void setRequestsPerSecond(double v) { this.requestsPerSecond = v; }
    public long getTotalDurationMs() { return totalDurationMs; }
    public void setTotalDurationMs(long v) { this.totalDurationMs = v; }
    public Map<Integer, Integer> getStatusCodeCounts() { return statusCodeCounts; }
    public void setStatusCodeCounts(Map<Integer, Integer> v) { this.statusCodeCounts = v; }
    public List<Long> getResponseTimes() { return responseTimes; }
    public void setResponseTimes(List<Long> v) { this.responseTimes = v; }
}
