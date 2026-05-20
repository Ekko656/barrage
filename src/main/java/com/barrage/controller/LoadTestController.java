package com.barrage.controller;
import com.barrage.model.LoadTestRequest;
import com.barrage.model.LoadTestResult;
import com.barrage.service.LoadTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/load-test")
@CrossOrigin(origins = "*")
public class LoadTestController {
    @Autowired
    private LoadTestService loadTestService;

    @PostMapping("/run")
    public ResponseEntity<LoadTestResult> runTest(@RequestBody LoadTestRequest request) {
        try {
            return ResponseEntity.ok(loadTestService.runTest(request));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Barrage is running");
    }
}
