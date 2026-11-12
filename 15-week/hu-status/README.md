<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       15-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 15

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Understand how to test a distributed system at scale (contract, integration, E2E, load and chaos) and what a production-grade release of MVP 3 requires.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | Study deliverables: testing at scale (contract, E2E, load, chaos); shipping MVP 3 as a production-grade release | done | See section 6 |
| N/A | Corte 3 certification (Huawei HCCDP-Solution Architectures): chapters 1 to 14, final exam (78 points, passed), certificate of completion, design note and two ADRs | done | [`certification/`](../certification/README.md), [e086d98](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/e086d98), [3306dcb](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/3306dcb) |

No implementation commits of mine are registered this week.

## 2. My individual contribution

- Studied why unit tests are not enough in a distributed system, the testing pyramid adapted to many services, contract tests, integration tests with real dependencies, end-to-end tests, load testing and chaos engineering. Summary and infographic are in this folder.
- Studied what production-grade means (reliable, observable, secure, operable), the production-readiness checklist, the release steps (tag, zero-downtime deploy, smoke checks, golden signals, keep or rollback) and the Definition of Done for MVP 3. Summary and infographic are in this folder.
- Relevance to OptiView: the hop table of the saga `place-order` (`opti-workflow` to `opti-customers-api`, `opti-products-api` and `opti-sales-api`) is where contract tests belong; the compensation of `reserve-stock` is the first chaos experiment (stop `opti-products-api` during a sale and check that the saga releases the stock); the rate limit of `opti-api-gateway` (30 requests per second) is the number to confirm with a load test.
- Certification: finished the 14 chapters and the final exam of the course (78 points, passed on 2026-11-12) and downloaded the certificate of completion. The four online labs were not done, with the teacher's approval. I also wrote the architecture design note, the target architecture diagram and the ADRs C3-01 (container cluster in two availability zones) and C3-02 (images tagged with the release version) for OptiView.
- The observability work of week 11 (SLOs, alerts and runbook) and the security gates of week 13 are the base of the production-readiness checklist.

## 3. Blockers and risks

- The certification has no labs: they were skipped with the teacher's approval, so that part of the evidence is missing. The study log has real dates only (three days), fewer than the guide asks for.
- The branch `feat/pdf-payments-validation` (four code repositories) has no pull requests yet, so its work is not in `develop`.
- No load or chaos test has been run on OptiView yet; they are plans, not results.

## 4. Plan for next week

- Submit the certification in Moodle before 2026-11-15.
- Write the first contract test between `opti-workflow` and `opti-products-api` for the reservation endpoint.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [ ] Testable acceptance criteria
- [ ] Tests added/updated (unit / integration)
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

Notes on unchecked items:
- This week's deliverables are study documents, so no HU branch, tests or acceptance criteria apply.

## 6. Evidence links

- Session summary: [`testing-at-scale-contract-e2e-load-chaos.md`](./testing-at-scale-contract-e2e-load-chaos.md)
- Session summary: [`release-shipping-mvp3-production-grade.md`](./release-shipping-mvp3-production-grade.md)
- Infographic: ![Testing at Scale](./testing-at-scale-contract-e2e-load-chaos-infographic.jpeg)
- Infographic: ![Release: Shipping MVP 3](./release-shipping-mvp3-production-grade-infographic.jpeg)
- Certification folder: [`certification/`](../certification/README.md)
