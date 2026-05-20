package com.barrage.model;
public class LoadTestRequest {
    private String url;
    private int requestCount;
    private int concurrency;
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getRequestCount() { return requestCount; }
    public void setRequestCount(int v) { this.requestCount = v; }
    public int getConcurrency() { return concurrency; }
    public void setConcurrency(int v) { this.concurrency = v; }
}
