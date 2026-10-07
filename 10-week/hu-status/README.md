<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       10-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 10

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Understand persistence patterns for distributed systems (database-per-service, Saga, Outbox, CQRS, eventual consistency) and the release process for shipping MVP 2 as one integrated system.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | Study deliverables: persistence patterns (Session 1) and release / MVP 2 (Session 2) | done | See section 6 |

## 2. My individual contribution

- Studied persistence in distributed systems: database-per-service, the Saga pattern with
  compensating actions, the Outbox pattern for reliable event publishing, CQRS, and eventual
  consistency. Summary and infographic are in this folder.
- Studied the release session: what a release is, MVP scope, integrated-system delivery, release
  steps (integrate, end-to-end tests, tag, deploy, verify) and good practices (semantic versioning,
  release notes, rollback plan). Summary and infographic are in this folder.
- Relevance to OptiView: the architecture already follows database-per-service (one schema per
  service, no cross-schema foreign keys, per ADR-002); the work-order approval flow
  (`ms-ordenes` -> `ms-inventario` stock decrement -> `OrdenCreada` event -> `ms-facturacion`) is
  the natural candidate for a Saga plus Outbox once a message broker is chosen.

## 3. Blockers and risks

- No message broker technology is chosen yet, so Outbox delivery and Saga orchestration remain
  design-level only for OptiView.
- Cut 1 runs as a monolith, so cross-service consistency is not exercised until the services are
  split.

## 4. Plan for next week

- Decide the message broker and record it as an ADR.
- Evaluate Saga (orchestration vs. choreography) for the work-order approval flow.
- Prepare the MVP 2 release: version tag, release notes and a rollback plan.

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

- Session summary: [`persistence-saga-outbox-cqrs.md`](./persistence-saga-outbox-cqrs.md)
- Session summary: [`release-shipping-mvp2.md`](./release-shipping-mvp2.md)
- Infographic: ![Persistence in Distributed Systems](./persistence-infographic.jpeg)
- Infographic: ![Release: Shipping an MVP of an Integrated System](./release-mvp-infographic.jpeg)
