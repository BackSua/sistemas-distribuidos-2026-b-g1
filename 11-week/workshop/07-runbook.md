# CP8 - Runbook: PlaceOrderFastBurn

Alert: `PlaceOrderFastBurn` (`06-alerts.yml`), severity `page`. Meaning: in the last hour more than
7.2 % of `POST /api/v1/sagas/place-order` requests of `opti-workflow` ended in a server error, and it
is still happening. At this rate the 30-day error budget is gone in 50 hours (`06-alert-policy.md`).

Platform: everything runs with Docker Compose from `opti-infra` (`compose.yml`). Service names:
`api-gateway`, `workflow`, `customers-api`, `products-api`, `sales-api`, `redis` (saga state),
`prometheus`, `grafana`. Run the commands from the `opti-infra` folder.

## 1. Symptoms

- Sellers see an error in the last step of the new-sale wizard of `opti-sales-portal` (HU-22).
- `api-gateway` answers 5xx on `/api/v1/sagas/place-order`.
- Sagas end `FAILED` with a technical reason, or stay `RUNNING` (`workflow_sagas_running` rising).
- Possible side effect: stock reserved in `products-api` without a quotation in `sales-api`.

## 2. Confirm

1. Error ratio now (PromQL, Grafana or Prometheus):
   `slo:place_order_error_ratio:rate5m` (alert threshold 0.072).
2. Is it only one hop? Compare the participants:
   `sum by (route) (rate(products_requests_total{outcome="error"}[5m]))`, the same with
   `customers_` and `sales_`.
3. Latency of the suspect: `histogram_quantile(0.95, sum by (le) (rate(products_request_duration_seconds_bucket[5m])))`.
4. Open one failed request by its `trace_id`:
   ```bash
   docker compose logs --since 30m workflow | grep '"event":"saga.failed"' | tail -5
   docker compose logs --since 30m workflow products-api customers-api sales-api api-gateway \
     | grep '"trace_id":"<TRACE_ID>"'
   ```
   Read the lines in order: the first `participant.call.timeout` or ERROR line names the failing
   `saga_step` (`check-patient`, `reserve-stock` or `open-order`), so the failing service.
5. Is the saga state store alive? `docker compose ps redis` and `docker compose logs --since 10m redis`.

## 3. First mitigations, from lowest to highest risk

| # | Action | Command or step | Risk | Use it when |
|---|---|---|---|---|
| 1 | Restart the failing participant | `docker compose restart products-api` (or `customers-api`, `sales-api`) | Low: sagas in `RUNNING` are resumed from Redis and every step has its own idempotency key `sagaId:step` | one participant times out or answers 5xx |
| 2 | Run two `workflow` instances | `docker compose up -d --scale workflow=2` | Low to medium: a Redis lease (`SAGA_LEASE`) keeps two instances from running the same saga | `workflow` itself is slow while participants are healthy |
| 3 | Roll back the last release of the failing service | `docker compose up -d <service>` with the previous image tag of that repository | Medium to high: check first that the database migration of the newer release is backward compatible | the alert started right after a deploy of that service |

Not available today, so do not try them: a feature flag to disable `place-order` and a circuit
breaker in `ParticipantClient` (only time-outs and bounded retries exist). Both are action items
of `08-incident.md`.

If a saga ended `FAILED` after the reservation, release the stock with the saga `cancel-order` of
`opti-workflow` or the release endpoint of `opti-products-api`; do not edit the database by hand.

## 4. Escalation

| Role | Who | When |
|---|---|---|
| First responder | the developer on duty of the team | page received; works steps 2 and 3 |
| Owner of the failing service | author of the last merged pull request in that repository (`opti-products-api`, `opti-customers-api`, `opti-sales-api` or `opti-workflow`) | after 15 minutes without improvement, or when mitigation 3 is needed |
| Team lead | the team member who coordinates the sprint | after 30 minutes, or when two services fail at once |
| Course reviewer | instructor of the course | only if the platform or repository access is blocked and the team cannot fix it |

## 5. Verify recovery

- `slo:place_order_error_ratio:rate5m` below 0.005 and `...rate1h` falling below 0.072.
- `workflow_sagas_running` back under 10 (SLO 3) and no saga stays `RUNNING` after the 30 s resume.
- One real test through the gateway: start `place-order` for a test patient; the answer is 2xx and
  the response carries the `traceparent` header.
- The alert resolves and stays resolved for 30 minutes (the confirmation window of the slow alert).
- Write the remaining budget: `1 - ((1 - <SLI>) / (1 - 0.995))` and open a postmortem like `08-incident.md`.
