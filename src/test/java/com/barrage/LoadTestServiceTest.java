package com.barrage;
import com.barrage.model.LoadTestRequest;
import com.barrage.model.LoadTestResult;
import com.barrage.service.LoadTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoadTestServiceTest {
    @Autowired
    private LoadTestService loadTestService;

    private LoadTestRequest req(String url, int count, int concurrency) {
        LoadTestRequest r = new LoadTestRequest();
        r.setUrl(url); r.setRequestCount(count); r.setConcurrency(concurrency);
        return r;
    }

    @Test
    void testTotalsAlwaysAddUp() throws InterruptedException {
        LoadTestResult result = loadTestService.runTest(req("https://httpbin.org/get", 5, 2));
        assertNotNull(result);
        assertEquals(5, result.getTotalRequests());
        assertEquals(result.getTotalRequests(), result.getSuccessCount() + result.getFailureCount());
    }

    @Test
    void testBadUrlFailsGracefully() throws InterruptedException {
        LoadTestResult result = loadTestService.runTest(req("https://this-does-not-exist-xyz9999.com", 3, 1));
        assertNotNull(result);
        assertEquals(3, result.getFailureCount());
        assertEquals(0, result.getSuccessCount());
    }

    @Test
    void testPercentilesAreValid() throws InterruptedException {
        LoadTestResult result = loadTestService.runTest(req("https://httpbin.org/get", 5, 2));
        if (result.getSuccessCount() > 0) {
            assertTrue(result.getP50() > 0);
            assertTrue(result.getP50() <= result.getP90());
            assertTrue(result.getP90() <= result.getP99());
        }
    }

    @Test
    void testRpsIsCalculated() throws InterruptedException {
        LoadTestResult result = loadTestService.runTest(req("https://httpbin.org/get", 5, 2));
        assertTrue(result.getRequestsPerSecond() > 0);
    }

    @Test
    void test404CountsAsFailure() throws InterruptedException {
        LoadTestResult result = loadTestService.runTest(req("https://httpbin.org/status/404", 4, 2));
        assertEquals(result.getTotalRequests(), result.getSuccessCount() + result.getFailureCount());
        assertEquals(4, result.getFailureCount());
    }
}
