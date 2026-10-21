# CP6 - SLIs, SLOs and error budget

Journey: `place-order` saga, `POST /api/v1/sagas/place-order` in `opti-workflow` (`01-journey.md`).
It touches `opti-customers-api` (`Patient`), `opti-products-api` (`Frame` reservation) and
`opti-sales-api` (`WorkOrder` in `QUOTATION`). Window of every SLO: 30 days, rolling. Metrics are
the ones of `04-metrics.md`.

A business refusal (for example `INSUFFICIENT_STOCK`, 4xx) is a correct answer, so only
`outcome="error"` counts as bad. A request that never reaches the saga (no valid token) is not a
valid event.

## Traffic estimate

OptiView serves one optical shop. Assumption, to be replaced by real data after the first month
of `/metrics`: 2 sellers, about 20 sales each per day, so **40 `place-order` requests per day**,
**1,200 per 30-day window**. A saga that retries a participant call still counts as one request.
The estimate is small on purpose: with so little traffic, a single bad day moves the numbers a lot,
so the targets below are not stricter than the shop can really notice.

## SLO 1 - Availability

| Item | Value |
|---|---|
| SLI | requests that did not end in a server error / valid requests |
| Target | **99.5 %** over 30 days |
| PromQL | `sum(rate(workflow_requests_total{route="/api/v1/sagas/place-order",outcome!="error"}[30d])) / sum(rate(workflow_requests_total{route="/api/v1/sagas/place-order"}[30d]))` |
| Budget in requests | 0.5 % of 1,200 = **6 failed requests** per window |
| Budget in minutes | 0.5 % of 43,200 min = **216 min** of full outage (90 min if only the 10 opening hours per day count: 0.5 % of 18,000 min) |

## SLO 2 - Latency

| Item | Value |
|---|---|
| SLI | requests answered in 1 s or less / all requests |
| Target | **95 %** under 1 s over 30 days |
| PromQL | `sum(rate(workflow_request_duration_seconds_bucket{route="/api/v1/sagas/place-order",le="1.0"}[30d])) / sum(rate(workflow_request_duration_seconds_count{route="/api/v1/sagas/place-order"}[30d]))` |
| Budget in requests | 5 % of 1,200 = **60 slow requests** per window |
| Budget in minutes | 5 % of 43,200 min = **2,160 min** with every request slow |

Why 1 s: a seller waits in the four-step wizard of `opti-sales-portal` (HU-22) with the patient
in front. Each participant call is limited to 5 s (`HTTP_TIMEOUT=PT5S`), so one timeout and one
retry already break the target, which is what we want to see.

## SLO 3 - No stuck sagas (specific to the product)

A saga stopped halfway holds a stock reservation of `opti-products-api` and leaves the quotation
unopened. The resume task runs every 30 s (`SAGA_RESUME_EVERY`).

| Item | Value |
|---|---|
| SLI | 5-minute slices where fewer than 10 sagas are `RUNNING` or `COMPENSATING` / all slices |
| Target | **99 %** of slices over 30 days |
| PromQL | `avg_over_time((max_over_time(workflow_sagas_running[5m]) < bool 10)[30d:5m])` |
| Budget in slices | 1 % of 8,640 slices = **86 slices** |
| Budget in minutes | 86 x 5 = **432 min** with stuck sagas |

The threshold of 10 is about a quarter of one day of sales: with 40 sales a day, ten sagas
waiting at once means something downstream is not answering.

## Remaining budget (same formula for the three SLOs)

For SLO 1: `1 - ((1 - <SLI>) / (1 - 0.995))`. At 1.0 the budget is untouched; at 0 it is spent.
Example: 3 failed requests in the window, SLI = 1197 / 1200 = 99.75 %, consumed = 0.25 / 0.5 =
50 %.

## Error budget policy

| Budget consumed | What the team does |
|---|---|
| 50 % | Review the failures of the week in the standup. New work on `opti-workflow`, `opti-products-api` and `opti-sales-api` continues, but each pull request names its rollback. |
| 75 % | Stop deploying features that touch the saga or the reservation. Only fixes, tests and the instrumentation of `04-metrics.md`. The owner of the failing service explains the cause in the next planning. |
| 100 % | Freeze releases to `qa` and `main` for the three services until the budget recovers. The team works only on reliability stories (see the postmortem action items in `08-incident.md`). Exceptions need a decision of the whole team. |
