# CP9 - Incident drill and blameless postmortem

## Scenario

Hop marked in CP1 as the one that can fail: `opti-workflow` -> `opti-products-api`,
`POST /api/v1/frames/{id}/reservations` (step `reserve-stock` of the saga `place-order`).
Drill: during 40 minutes, from 14:10 to 14:50, 25 % of the reservation calls answer in 3 seconds
instead of tens of milliseconds. The call limit of `ParticipantClient` is 5 s (`HTTP_TIMEOUT=PT5S`),
so these calls are slow but they succeed: there is no timeout and no retry.

Traffic during the drill (assumption, busy afternoon): 10 `place-order` requests per hour, so
7 requests in the 40 minutes, 2 of them slow (25 % of 7 is 1.75, rounded up).

## Trace of one slow request

`trace_id` 3a9c5e7d1b2f4086a4d9e0c1f7b83d25, started 14:21:08 by `opti-sales-portal`.

```
place-order  [3,412 ms]
|- http POST /api/v1/sagas/place-order  [opti-api-gateway]  3,412 ms
|  `- saga.place-order  [opti-workflow]  3,398 ms
|     |- saga.step check-patient  38 ms
|     |  `- http GET /api/v1/patients/{id}  [opti-customers-api]  31 ms
|     |- saga.step reserve-stock  3,010 ms          <- the slow hop
|     |  `- http POST /api/v1/frames/{id}/reservations  [opti-products-api]  3,004 ms
|     `- saga.step open-order  142 ms
|        `- http POST /api/v1/work-orders  [opti-sales-api]  131 ms
```

## Four log lines of that trace (CP2 format)

```json
{"timestamp":"2026-10-21T14:21:08.020Z","level":"INFO","service":"opti-api-gateway","version":"1.1.0","environment":"main","trace_id":"3a9c5e7d1b2f4086a4d9e0c1f7b83d25","span_id":"2c4e6a8b0d1f3a5c","correlation_id":"c-90a1d3","event":"http.request.received","message":"POST /api/v1/sagas/place-order"}
{"timestamp":"2026-10-21T14:21:11.049Z","level":"WARN","service":"opti-products-api","version":"1.1.0","environment":"main","trace_id":"3a9c5e7d1b2f4086a4d9e0c1f7b83d25","span_id":"7b1d3f5a9c2e4d60","correlation_id":"c-90a1d3","event":"http.request.completed","message":"POST /api/v1/frames/{id}/reservations","duration_ms":3004,"status":201,"product_type":"FRAME"}
{"timestamp":"2026-10-21T14:21:11.055Z","level":"INFO","service":"opti-workflow","version":"1.1.0","environment":"main","trace_id":"3a9c5e7d1b2f4086a4d9e0c1f7b83d25","span_id":"5e7a9c1b3d4f6082","correlation_id":"c-90a1d3","event":"saga.step.completed","message":"stock reserved","saga_id":"6f21b8d4","saga_step":"reserve-stock","duration_ms":3010}
{"timestamp":"2026-10-21T14:21:11.430Z","level":"INFO","service":"opti-api-gateway","version":"1.1.0","environment":"main","trace_id":"3a9c5e7d1b2f4086a4d9e0c1f7b83d25","span_id":"2c4e6a8b0d1f3a5c","correlation_id":"c-90a1d3","event":"http.request.completed","message":"POST /api/v1/sagas/place-order -> 200","duration_ms":3412,"work_order_code":"OT-0188"}
```

## SLI values reached

| SLO (`05-slo.md`) | During the 40 minutes | Whole 30-day window (1,200 requests) | Budget used |
|---|---|---|---|
| 1 Availability 99.5 % | 100 % (no server error) | 100 % | 0 of 6 failed requests |
| 2 Latency 95 % under 1 s | 5 of 7 under 1 s = 71 % | 1,198 of 1,200 = 99.83 % | 2 of 60 slow requests = **3.3 %**; in time terms 25 % x 40 = 10 of 2,160 minutes = 0.46 % |
| 3 No stuck sagas 99 % | `workflow_sagas_running` peaks at 1 | 100 % of slices | 0 of 86 slices |

## Which alert fires and when

**None.** `PlaceOrderFastBurn` and `PlaceOrderSlowBurn` watch server errors and there are none.
Even a latency alert with the same burn-rate method would not fire: 25 % slow requests against a
5 % budget is a burn rate of 25 / 5 = 5, below 6 (slow) and far below 14.4 (fast), and 40 minutes is
too short to move a 6-hour window. The first signal was a person: a seller told the team at 14:25,
minute 15. The drill shows a real gap: the sellers wait 3 s and no pager knows.

Runbook step that stops it: step 1 of `07-runbook.md`, `docker compose restart products-api`, once
the slow hop is located with the trace above (confirm step 4).

## Blameless postmortem

**Timeline**

| Time | Event |
|---|---|
| 14:10 | 25 % of reservations in `opti-products-api` start answering in about 3 s |
| 14:25 | A seller reports that the sale wizard "hangs a few seconds" |
| 14:31 | The person on duty reads p95 of the reservation route = 3.0 s and opens trace 3a9c5e7d... |
| 14:33 | The trace shows `reserve-stock` = 3,010 ms of 3,412 ms; customers and sales are normal |
| 14:46 | Restart of `products-api` (runbook step 1) |
| 14:50 | Answers back to tens of milliseconds; p95 under 1 s again |

**Impact:** 2 slow requests of 7 (25 %), 40 minutes, no failed request, no stock left reserved.
Latency budget used 3.3 % of 60 requests. Availability budget used 0 %.

**Root cause (hypothesis of the drill):** the reservation locks the stock row of the `Frame`, and a
long inventory summary query (HU-15) held it, so some reservations waited about 3 s.

**Contributing factors:** the saga has no per-step duration metric, only the request total; no
alert watches the latency of the reservation; the 5 s limit hides a 3 s answer as "success".

**What went well:** the `trace_id` located the slow hop in two minutes; the saga never failed and
no compensation was needed; restarting was safe because every step carries an idempotency key.

## Action items (user stories)

1. **As the person on duty, I want a ticket alert when the p95 of the reservation route stays above
   2 s for 10 minutes, so that I learn about slow stock before the sellers do.**
   Acceptance: given a rule test where `products_request_duration_seconds` p95 for
   `/api/v1/frames/{id}/reservations` is 3 s for 10 minutes, the alert `ReserveStockSlow`
   (severity `ticket`) fires in at most 12 minutes; with p95 at 0.2 s it does not fire.
2. **As a developer, I want a histogram of each saga step in `opti-workflow`, so that the slow hop
   is visible without opening traces.**
   Acceptance: after one `place-order`, `/metrics` shows `workflow_saga_step_duration_seconds_bucket`
   with `step` values `check-patient`, `reserve-stock` and `open-order`; a unit test asserts the
   three values and that `saga_id` is not a label.
3. **As a seller, I want `place-order` to answer fast with a clear message when stock is slow, so
   that I do not wait 3 s on every sale.**
   Acceptance: after 5 consecutive reservation calls above 2 s, the breaker opens for 30 s and the
   saga ends with `failureReason=STOCK_UNAVAILABLE` in under 200 ms; after 30 s one test call is
   let through; a test with a fake slow participant covers open, half-open and closed.
