# Barrage
### API Load Testing & Performance Monitor

Fire configurable bursts of concurrent HTTP requests at any REST endpoint and get an instant breakdown — response timelines, latency percentiles (P50/P90/P99), success rates, status code distribution, and a run history — all in a live dashboard.

---

## Tech Stack

| Layer       | Technology                              |
|-------------|-----------------------------------------|
| Language    | Java 17                                 |
| Framework   | Spring Boot 3.2                         |
| REST API    | Spring MVC (`@RestController`)          |
| Concurrency | `ExecutorService`, `AtomicInteger`      |
| Frontend    | HTML5, CSS, JavaScript, jQuery          |
| Build       | Maven                                   |
| Container   | Docker (multi-stage, Alpine)            |

---

## Getting Started

**Prerequisites:** Java 17+, Maven

```bash
git clone https://github.com/YOUR_USERNAME/barrage.git
cd barrage
mvn spring-boot:run
```

Open `http://localhost:8080` in your browser.

---

## Docker

```bash
docker build -t barrage .
docker run -p 8080:8080 barrage
```

The multi-stage Dockerfile uses a Maven Alpine build stage and a lightweight Eclipse Temurin 17 JRE Alpine runtime image (~180MB final image).

---

## Live Demo

**[barrage-0ajs.onrender.com](https://barrage-0ajs.onrender.com)**

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
  "p50": 130.0,
  "p90": 205.0,
  "p99": 310.0,
  "requestsPerSecond": 8.3,
  "totalDurationMs": 2410,
  "statusCodeCounts": { "200": 19, "0": 1 },
  "responseTimes": [112, 98, 143, 201, "..."]
}
```

### `GET /api/load-test/health`
Returns `200 OK` — confirms the server is up.

---

## Project Structure

```
barrage/
├── Dockerfile
├── pom.xml
└── src/
    └── main/
        ├── java/com/barrage/
        │   ├── BarrageApplication.java
        │   ├── controller/
        │   │   └── LoadTestController.java
        │   ├── service/
        │   │   └── LoadTestService.java
        │   └── model/
        │       ├── LoadTestRequest.java
        │       └── LoadTestResult.java
        └── resources/
            └── static/
                └── index.html
```
