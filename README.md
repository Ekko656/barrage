# Salvo ⚡
### API Load Testing & Performance Monitor

A load testing tool built with Java 17 and Spring Boot. Point it at any REST API, fire off a configurable number of concurrent requests, and get a real-time breakdown — response times, success rates, failure counts, and a response time distribution chart.

---

## Tech Stack

| Layer      | Technology                              |
|------------|-----------------------------------------|
| Language   | Java 17                                 |
| Framework  | Spring Boot 3.2                         |
| REST API   | Spring MVC (`@RestController`)          |
| Concurrency| `ExecutorService`, `AtomicInteger`      |
| Frontend   | HTML5, CSS, JavaScript, jQuery          |
| Testing    | JUnit 5, Spring Boot Test               |
| Build      | Maven                                   |

---

## Getting Started

**Prerequisites:** Java 17+, Maven

```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/salvo.git
cd salvo

# Run the app
mvn spring-boot:run
```

Open `http://localhost:8080` in your browser.

---

## Running the Tests

```bash
mvn test
```

The test suite covers:
- Result object is returned with correct totals
- Invalid/unreachable URLs produce failures gracefully, no crashes
- Response time stats are mathematically valid (min ≤ avg ≤ max)
- 404 responses are correctly counted as failures

---

## API Reference

### `POST /api/load-test/run`
Runs a load test against the given URL.

**Request:**
```json
{
  "url": "https://api.example.com/endpoint",
  "requestCount": 20,
  "concurrency": 5
}
```

**Response:**
```json
{
  "url": "https://api.example.com/endpoint",
  "totalRequests": 20,
  "successCount": 19,
  "failureCount": 1,
  "avgResponseTimeMs": 142.3,
  "minResponseTimeMs": 98.0,
  "maxResponseTimeMs": 310.0,
  "responseTimes": [112, 98, 143, 201, "..."]
}
```

### `GET /api/load-test/health`
Returns `200 OK` — confirms the server is up.

---

## Project Structure

```
src/
├── main/
│   ├── java/com/salvo/
│   │   ├── SalvoApplication.java        # App entry point
│   │   ├── controller/
│   │   │   └── LoadTestController.java      # REST endpoints
│   │   ├── service/
│   │   │   └── LoadTestService.java         # Concurrent load test logic
│   │   └── model/
│   │       ├── LoadTestRequest.java         # Request shape
│   │       └── LoadTestResult.java          # Response shape
│   └── resources/
│       └── static/
│           └── index.html                   # Dashboard (HTML/CSS/JS/jQuery)
└── test/
    └── java/com/salvo/
        └── LoadTestServiceTest.java         # JUnit 5 test suite
```
