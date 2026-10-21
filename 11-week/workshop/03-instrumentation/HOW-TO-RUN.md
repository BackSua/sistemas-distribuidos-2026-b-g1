# How to run the workflow-trace module

Small Java 21 module (JDK only, no framework) that adds to `opti-workflow` the pieces described in
`../02-log-contract.md`: W3C `traceparent` middleware, JSON logger, propagation to a participant
(`opti-products-api`) and two Prometheus metrics. The test proves that the `trace_id` received by the
workflow is the one sent downstream.

## Requirements

- JDK 21 or newer (tried with JDK 25.0.3).
- JUnit 5.11.4 jars (jupiter api and engine, platform launcher, engine and commons, opentest4j,
  apiguardian). They come from the local Maven repository (`~/.m2/repository`) of the team repos.

## Build and test (Git Bash on Windows)

```bash
cd 11-week/workshop/03-instrumentation/workflow-trace
M=$(cygpath -m ~/.m2/repository)
CP="$M/org/junit/platform/junit-platform-launcher/1.11.4/junit-platform-launcher-1.11.4.jar;$M/org/junit/platform/junit-platform-engine/1.11.4/junit-platform-engine-1.11.4.jar;$M/org/junit/platform/junit-platform-commons/1.11.4/junit-platform-commons-1.11.4.jar;$M/org/junit/jupiter/junit-jupiter-engine/5.11.4/junit-jupiter-engine-5.11.4.jar;$M/org/junit/jupiter/junit-jupiter-api/5.11.4/junit-jupiter-api-5.11.4.jar;$M/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar;$M/org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar"
OUT=$(cygpath -m /tmp/w11out); mkdir -p /tmp/w11out
javac --release 21 -cp "$CP" -d "$OUT" $(find src -name '*.java')
java -cp "$OUT;$CP" co.edu.corhuila.opti.workflow.trace.RunTests
```

On Linux or macOS use `:` as classpath separator and plain paths.

## Real output of the test run

```text

Test run finished after 592 ms
[         2 containers found      ]
[         0 containers skipped    ]
[         2 containers started    ]
[         0 containers aborted    ]
[         2 containers successful ]
[         0 containers failed     ]
[         3 tests found           ]
[         0 tests skipped         ]
[         3 tests started         ]
[         0 tests aborted         ]
[         3 tests successful      ]
[         0 tests failed          ]

```

## Real output of the demo (one request, then /metrics)

```bash
java -cp "$OUT;$CP" co.edu.corhuila.opti.workflow.trace.DemoRun
```

```text
{"timestamp":"2026-10-20T23:54:26.679Z","level":"INFO","service":"opti-workflow","version":"1.1.0","environment":"dev","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"afc2b33eed8d98da","correlation_id":"3ba85581-b673-4bd3-8de7-e02ef92f4d1d","event":"saga.step.started","message":"reserve-stock","saga_step":"reserve-stock"}
{"timestamp":"2026-10-20T23:54:26.751Z","level":"INFO","service":"opti-workflow","version":"1.1.0","environment":"dev","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"afc2b33eed8d98da","correlation_id":"3ba85581-b673-4bd3-8de7-e02ef92f4d1d","event":"saga.step.completed","message":"reserve-stock answered 201","saga_step":"reserve-stock"}
{"timestamp":"2026-10-20T23:54:26.754Z","level":"INFO","service":"opti-workflow","version":"1.1.0","environment":"dev","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"afc2b33eed8d98da","correlation_id":"3ba85581-b673-4bd3-8de7-e02ef92f4d1d","event":"http.request.completed","message":"POST /api/v1/sagas/place-order","duration_ms":72,"status":200}
response traceparent: 00-7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72-afc2b33eed8d98da-01
# HELP workflow_requests_total Requests handled, by route and outcome.
# TYPE workflow_requests_total counter
workflow_requests_total{route="/api/v1/sagas/place-order",outcome="ok"} 1
# HELP workflow_request_duration_seconds Request latency in seconds.
# TYPE workflow_request_duration_seconds histogram
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="0.05"} 0
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="0.1"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="0.25"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="0.5"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="1.0"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="2.5"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="5.0"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="10.0"} 1
workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="+Inf"} 1
workflow_request_duration_seconds_sum{route="/api/v1/sagas/place-order"} 0.0728125
workflow_request_duration_seconds_count{route="/api/v1/sagas/place-order"} 1

done
```
