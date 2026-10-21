# CP2 - Structured log contract

Every OptiView service writes one JSON object per line on stdout, with these fields. The same
contract is used by `opti-api-gateway`, `opti-workflow`, `opti-customers-api`, `opti-products-api`
and `opti-sales-api`, so one query by `trace_id` rebuilds the story of a `place-order` request
(journey in `01-journey.md`).

## Mandatory fields

| Field | Type | Req. | Example | Why it exists |
|---|---|---|---|---|
| `timestamp` | string ISO-8601 UTC | yes | `2026-10-20T19:41:07.318Z` | orders events across services |
| `level` | string | yes | `WARN` | filter by severity |
| `service` | string | yes | `opti-workflow` | which repository wrote it |
| `version` | string | yes | `1.1.0` | links a line to the release (`release/1.1.0`) |
| `environment` | string | yes | `qa` | `dev`, `qa` or `main` environments |
| `trace_id` | 32 hex | yes | `7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72` | same value in every service of one request |
| `span_id` | 16 hex | yes | `a1b2c3d4e5f60718` | the hop that wrote the line |
| `correlation_id` | string | yes | `5b9e0c52-...` | existing `X-Correlation-Id`, kept for the current tools |
| `event` | string | yes | `participant.call.timeout` | stable name, used for alerts |
| `message` | string | yes | `opti-products-api did not answer` | human text without personal data |
| `duration_ms` | integer | no | `5003` | latency of the operation described |

## Domain fields (OptiView)

| Field | Example | Why |
|---|---|---|
| `saga_id` | `0d5f...` | identifies the `place-order` saga; it is also the idempotency prefix `sagaId:step` |
| `saga_step` | `reserve-stock` | one of `check-patient`, `reserve-stock`, `open-order` |
| `product_type` | `FRAME` | catalogue of the reservation (frame, lens, accessory, liquid) |
| `patient_id` | `9a41...` | UUID of the `Patient`, never the document or the name |
| `work_order_code` | `OT-0042` | business number of the `WorkOrder` once it exists |
| `failure_reason` | `INSUFFICIENT_STOCK` | closed list returned by the saga |

## Never log

| Data | Masking |
|---|---|
| Patient document number, name, phone, email, address | not written; only `patient_id` (UUID) |
| Optical formula values (sphere, cylinder, axis) - health data | not written; only the formula id |
| Card, bank or Wompi payment reference | not written; only `transaction_id` |
| `Authorization` header, access, refresh and service tokens | never; at most the last 4 characters of the `jti` |
| Passwords and bcrypt hashes of `opti-auth-api` | never |
| Database URL with credentials, Wompi and gateway keys | never; log only the host name |
| Request and response bodies | never; log the size in bytes |

## Seven lines of one failed request (same `trace_id`)

```json
{"timestamp":"2026-10-20T19:41:07.102Z","level":"INFO","service":"opti-api-gateway","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"1111aaaa2222bbbb","correlation_id":"c-5b9e0c52","event":"http.request.received","message":"POST /api/v1/sagas/place-order"}
{"timestamp":"2026-10-20T19:41:07.131Z","level":"INFO","service":"opti-workflow","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"3333cccc4444dddd","correlation_id":"c-5b9e0c52","event":"saga.started","message":"place-order started","saga_id":"0d5f77c1","product_type":"FRAME","patient_id":"9a41e0b2"}
{"timestamp":"2026-10-20T19:41:07.176Z","level":"INFO","service":"opti-workflow","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"5555eeee6666ffff","correlation_id":"c-5b9e0c52","event":"saga.step.completed","message":"patient is active","saga_id":"0d5f77c1","saga_step":"check-patient","duration_ms":41}
{"timestamp":"2026-10-20T19:41:12.190Z","level":"WARN","service":"opti-workflow","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"7777aaaa8888bbbb","correlation_id":"c-5b9e0c52","event":"participant.call.timeout","message":"opti-products-api did not answer, retrying","saga_id":"0d5f77c1","saga_step":"reserve-stock","duration_ms":5003}
{"timestamp":"2026-10-20T19:41:12.480Z","level":"WARN","service":"opti-products-api","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"9999ccccaaaadddd","correlation_id":"c-5b9e0c52","event":"stock.reservation.rejected","message":"not enough units to reserve","product_type":"FRAME","failure_reason":"INSUFFICIENT_STOCK","duration_ms":38}
{"timestamp":"2026-10-20T19:41:12.512Z","level":"ERROR","service":"opti-workflow","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"bbbb1111cccc2222","correlation_id":"c-5b9e0c52","event":"saga.failed","message":"place-order ended FAILED, nothing to compensate","saga_id":"0d5f77c1","saga_step":"reserve-stock","failure_reason":"INSUFFICIENT_STOCK","duration_ms":5410}
{"timestamp":"2026-10-20T19:41:12.530Z","level":"INFO","service":"opti-api-gateway","version":"1.1.0","environment":"qa","trace_id":"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72","span_id":"1111aaaa2222bbbb","correlation_id":"c-5b9e0c52","event":"http.request.completed","message":"POST /api/v1/sagas/place-order -> 422","duration_ms":5428}
```

Reading by `trace_id`: the patient was fine, the first reservation call timed out after 5 s, the
retry reached `opti-products-api`, which refused it for lack of stock, and the saga ended `FAILED`.

## Levels

| Level | Use | Example in OptiView |
|---|---|---|
| ERROR | the operation failed and a person may have to act | `saga.failed`, a compensation that fails |
| WARN | a recoverable problem or a business refusal | a participant timeout, `INSUFFICIENT_STOCK` |
| INFO | normal milestones of a request | `saga.step.completed`, `http.request.completed` |
| DEBUG | detail for diagnosis, off in `main` | the retry back-off value chosen for a call |
