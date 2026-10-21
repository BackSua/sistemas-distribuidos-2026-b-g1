# CP4 - Golden signals and metric catalog

Journey: `place-order` saga (`01-journey.md`). One table per service of the journey. Names follow
Prometheus rules: `snake_case`, base unit as suffix (`_seconds`), `_total` for counters. The
workflow metrics are the ones implemented in `03-instrumentation/workflow-trace`
(`workflow_requests_total`, `workflow_request_duration_seconds`); the other services use the same
shape with their own prefix, so every dashboard panel is the same query with another prefix. Only the two workflow metrics
exist in code (CP3); the rest is the target catalog the other repositories must implement.

Closed label values used below: `outcome` is `ok`, `client_error` or `error`; `route` is the
registered path template (for example `/api/v1/work-orders/{id}`), never the raw URL.

## opti-api-gateway (`api-gateway`, nginx)

| Signal | Metric | Type | Labels | PromQL for a dashboard |
|---|---|---|---|---|
| Latency | `gateway_request_duration_seconds` | histogram | `route` | `histogram_quantile(0.95, sum by (le) (rate(gateway_request_duration_seconds_bucket{route="/api/v1/sagas"}[5m])))` |
| Traffic | `gateway_requests_total` | counter | `route`, `outcome` | `sum by (route) (rate(gateway_requests_total[5m]))` |
| Errors | `gateway_requests_total` | counter | `route`, `outcome` | `sum(rate(gateway_requests_total{outcome="error"}[5m])) / sum(rate(gateway_requests_total[5m]))` |
| Saturation | `gateway_connections_active` | gauge | none | `max_over_time(gateway_connections_active[5m]) / 1024` |

## opti-workflow (saga orchestrator, `ms-ordenes` side of HU-08)

| Signal | Metric | Type | Labels | PromQL for a dashboard |
|---|---|---|---|---|
| Latency | `workflow_request_duration_seconds` | histogram | `route` | `histogram_quantile(0.95, sum by (le) (rate(workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order"}[5m])))` |
| Traffic | `workflow_requests_total` | counter | `route`, `outcome` | `sum(rate(workflow_requests_total{route="/api/v1/sagas/place-order"}[5m]))` |
| Errors | `workflow_requests_total` | counter | `route`, `outcome` | `sum(rate(workflow_requests_total{outcome="error"}[5m])) / sum(rate(workflow_requests_total[5m]))` |
| Saturation | `workflow_sagas_running` | gauge | none | `max_over_time(workflow_sagas_running[5m])` (sagas in `RUNNING` or `COMPENSATING`; they wait on `SAGA_RESUME_AFTER`, 30 s) |

## opti-customers-api (`ms-pacientes`, `Patient`)

| Signal | Metric | Type | Labels | PromQL for a dashboard |
|---|---|---|---|---|
| Latency | `customers_request_duration_seconds` | histogram | `route` | `histogram_quantile(0.95, sum by (le) (rate(customers_request_duration_seconds_bucket{route="/api/v1/patients/{id}"}[5m])))` |
| Traffic | `customers_requests_total` | counter | `route`, `outcome` | `sum by (route) (rate(customers_requests_total[5m]))` |
| Errors | `customers_requests_total` | counter | `route`, `outcome` | `sum(rate(customers_requests_total{outcome="error"}[5m])) / sum(rate(customers_requests_total[5m]))` |
| Saturation | `customers_db_connections_active` | gauge | none | `max_over_time(customers_db_connections_active[5m]) / max(customers_db_connections_max)` |

## opti-products-api (`ms-inventario`, `Frame` reservations)

| Signal | Metric | Type | Labels | PromQL for a dashboard |
|---|---|---|---|---|
| Latency | `products_request_duration_seconds` | histogram | `route` | `histogram_quantile(0.95, sum by (le) (rate(products_request_duration_seconds_bucket{route="/api/v1/frames/{id}/reservations"}[5m])))` |
| Traffic | `products_requests_total` | counter | `route`, `outcome` | `sum(rate(products_requests_total{route="/api/v1/frames/{id}/reservations"}[5m]))` |
| Errors | `products_reservations_total` | counter | `product_type`, `outcome` | `sum(rate(products_reservations_total{outcome="rejected"}[5m])) / sum(rate(products_reservations_total[5m]))` |
| Saturation | `products_db_connections_active` | gauge | none | `max_over_time(products_db_connections_active[5m]) / max(products_db_connections_max)` |

`products_reservations_total` uses its own outcome list (`reserved`, `rejected`, `released`):
a rejection for lack of stock is a business answer, not an HTTP error.

## opti-sales-api (`ms-ordenes`, `WorkOrder`)

| Signal | Metric | Type | Labels | PromQL for a dashboard |
|---|---|---|---|---|
| Latency | `sales_request_duration_seconds` | histogram | `route` | `histogram_quantile(0.95, sum by (le) (rate(sales_request_duration_seconds_bucket{route="/api/v1/work-orders"}[5m])))` |
| Traffic | `sales_requests_total` | counter | `route`, `outcome` | `sum(rate(sales_requests_total{route="/api/v1/work-orders"}[5m]))` |
| Errors | `sales_requests_total` | counter | `route`, `outcome` | `sum(rate(sales_requests_total{outcome="error"}[5m])) / sum(rate(sales_requests_total[5m]))` |
| Saturation | `sales_db_connections_active` | gauge | none | `max_over_time(sales_db_connections_active[5m]) / max(sales_db_connections_max)` |

## Cardinality

| Label | Values | Estimate | Rule |
|---|---|---|---|
| `route` | registered path templates of one service | at most 12 | path template only, never the raw URL |
| `outcome` | `ok`, `client_error`, `error` | 3 | closed list |
| `product_type` | `FRAME`, `LENS`, `ACCESSORY`, `LIQUID` | 4 | closed list |
| `le` | 8 buckets plus `+Inf` | 9 | fixed |

Per service: counter 12 x 3 = 36 series; histogram 12 x (9 + sum + count) = 132 series; about 170
series, so five services stay under 1,000 series.

Forbidden labels: `patient_id`, `user_id`, `email`, document number, `saga_id`, `trace_id`,
`Idempotency-Key`, `reservation_id` and any raw URL with ids. Each of them has millions of values.
They go in the log line (`02-log-contract.md`) and are found by `trace_id`; the metric keeps
`route` and `product_type` instead.

## Histogram buckets

`0.05, 0.1, 0.25, 0.5, 1, 2.5, 5, 10` seconds. The saga calls each participant with a limit of 5 s
(`HTTP_TIMEOUT=PT5S`) and a seller waiting in the sales wizard (HU-22) expects the answer in under
one second; a participant call is expected to take tens of milliseconds.
Fine buckets from 50 ms to 1 s resolve the target (p95 under 1 s); the bucket at 5 s marks the
timeout and 10 s catches a request that spent two attempts.
