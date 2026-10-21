<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       11-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 11

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Make the place-order saga of OptiView observable end to end (trace map, structured log contract, instrumentation code with a test, metric catalog) and learn observability, SLIs and SLOs.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | W11 workshop, delivery 1: critical journey and trace map (CP1) | done | [77c99aa](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/77c99aaba4fb8ea7946d0f6ec17ee334ff246a0f) |
| N/A | W11 workshop, delivery 1: structured log contract (CP2) | done | [14cdc77](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/14cdc77ac9e01461d8a248daa7fde3550ebf38a8) |
| N/A | W11 workshop, delivery 1: instrumentation code and test (CP3) | done | [b2304d3](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/b2304d3fbf7cf158d5892b59631bcfe40ff329c1), [43f8dbd](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/43f8dbdca5f8054332103794c9544f5410fc1b10) |
| N/A | W11 workshop, delivery 1: golden signals and metric catalog (CP4) | done | [7988074](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/7988074e0f06d82b2d796e96158011679e1dbd27) |
| N/A | W11 workshop, delivery 2: SLIs, SLOs and error budget (CP6) | done | [ea33c1d](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/ea33c1d9340cca7d827f8ddd114347fc7dc909ff) |
| N/A | W11 workshop, delivery 2: burn-rate alerts and alert policy (CP7) | done | [57c05d6](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/57c05d6f32519cf8cc8cdaac301dfde268cb1579) |
| N/A | W11 workshop, delivery 2: runbook for the fast-burn alert (CP8) | done | [dc9a97e](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/dc9a97e5f8fb2f697f2ab84979251b5cfb19fc3a) |
| N/A | W11 workshop, delivery 2: incident drill and postmortem (CP9) | done | [27f03e4](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/27f03e47bf7a2f2101405c7bfbdaa79a2034d6d9) |
| N/A | Study deliverables: observability and SLIs/SLOs | done | See section 6 |

## 2. My individual contribution

- Chose the `place-order` saga as the critical journey: `opti-workflow` calls `opti-customers-api`,
  `opti-products-api` and `opti-sales-api`, and a background task resumes stopped sagas. Wrote the hop
  table, the sequence diagram with `traceparent` and the span tree.
- Defined the JSON log contract (mandatory and domain fields, a never-log list that includes
  patient documents and optical formula values, seven lines of one failed request).
- Built a small Java module (`03-instrumentation/workflow-trace`): trace-context middleware,
  structured logger, propagation to the `opti-products-api` call, a request counter and a latency
  histogram on /metrics, and a test proving the received `trace_id` is the one sent downstream.
- Wrote the metric catalog with the four golden signals per service, the cardinality rules and the
  histogram buckets tied to the 5 s HTTP_TIMEOUT of the workflow.
- Delivery 2: defined three SLOs for place-order (availability 99.5 %, latency 95 % under 1 s, no
  stuck sagas) with the error budget in requests and minutes and a budget policy; wrote the
  burn-rate alert rules (validated as YAML, not with promtool) and their policy; wrote the runbook
  with the mitigations that Docker Compose in opti-infra really supports; ran an incident drill
  whose result is that no alert fires, and turned it into three action items as user stories.
- Studied the two Week 11 sessions: logs, metrics and traces; SLIs, SLOs, error budgets and
  alerting. Summaries are in this folder.

## 3. Blockers and risks

- Only X-Correlation-Id is propagated today in the real services; the workshop module is not yet
  integrated into `opti-workflow`.
- No metrics endpoint or log contract is applied in the other repositories yet.
- The alert rules were not run with promtool, and the Prometheus of opti-infra scrapes only the
  otel-collector today.
- The traffic of 40 sales per day is an estimate; the SLO targets need real data.

## 4. Plan for next week

- Propose to the team the three action items of the incident drill as stories for the next sprint.
- Apply the trace propagation to the stock reservation call first, then expose the request
  counter in opti-workflow and add the scrape job in opti-infra.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration)
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

Notes on unchecked items:
- The workshop is individual and lives in the course fork, not in the product repositories.

## 6. Evidence links

- Workshop folder: [11-week/workshop](../workshop/README.md) (delivery 1: CP0-CP5, delivery 2: CP6-CP10)
- Reflection document: https://docs.google.com/document/d/1fe9Ukmpc1wv11txT-ylOU1DVqmySCSdVWHW3wXSnSLc/edit
- Session summary: [`observability-logs-metrics-tracing.md`](./observability-logs-metrics-tracing.md)
- Session summary: [`reliability-slis-slos.md`](./reliability-slis-slos.md)
