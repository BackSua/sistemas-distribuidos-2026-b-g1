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
| HU-13 | Staff notifications | doing | [opti-auth-api@789b64c](https://github.com/code-corhuila/opti-auth-api/commit/789b64ce7a5df728600edd04c6ba27a618d08525), [opti-front@88e3397](https://github.com/code-corhuila/opti-front/commit/88e3397d3666390a593754ee45ae96aaa0fb311b) |
| HU-14 | Dashboard metrics, charts and quick links | doing | [opti-front@0af50b8](https://github.com/code-corhuila/opti-front/commit/0af50b828c8c8e6780a12aa4ad1388408a2b588b) |
| HU-15 | Inventory summary and brand filtering | doing | [opti-products-api@5f4fcd4](https://github.com/code-corhuila/opti-products-api/commit/5f4fcd4cd63e199930e8abcb70b86560e12cd2af), [opti-products-portal@2d793b2](https://github.com/code-corhuila/opti-products-portal/commit/2d793b2bd422ee89d1f025a35642651f6a08d977) |
| HU-16 | Frame photo upload and display | doing | [opti-products-api@f9bde5d](https://github.com/code-corhuila/opti-products-api/commit/f9bde5d44ef8aa3da2e9d606867470cb0722dac1), [opti-products-db@4592918](https://github.com/code-corhuila/opti-products-db/commit/45929183cdfb86ea0c48ca76fb5435380bad41be), [opti-products-portal@9050151](https://github.com/code-corhuila/opti-products-portal/commit/9050151b1f8f176706dc100463cd416b89a0665c), [opti-api-gateway@47e2db0](https://github.com/code-corhuila/opti-api-gateway/commit/47e2db0c72365ec7fc94c2ad3f80706bc1fc8796) |
| HU-17 | Patients summary and avatars | doing | [opti-customers-api@62214c6](https://github.com/code-corhuila/opti-customers-api/commit/62214c61f0c1caef3892c98910098f3c690bd48c), [opti-customers-portal@703c5f0](https://github.com/code-corhuila/opti-customers-portal/commit/703c5f073cb78a55a69705c424e6468107378ed0), [opti-front@f6fd44c](https://github.com/code-corhuila/opti-front/commit/f6fd44cfd7b0a59451d8d4b7b805897053d55a71) |
| HU-18 | Patient form sections | doing | [opti-customers-portal@4db94ba](https://github.com/code-corhuila/opti-customers-portal/commit/4db94bac73fdc2e4224710b4f3815f67b1267aaf) |
| HU-19 | Optional user email | doing | [opti-auth-api@669b820](https://github.com/code-corhuila/opti-auth-api/commit/669b820144b933e1c7a60241a8849a135a1c889a), [opti-auth-db@ec92f2a](https://github.com/code-corhuila/opti-auth-db/commit/ec92f2a8e01b2efd9fe7212fad3182256d131a8e), [opti-auth-portal@eac2745](https://github.com/code-corhuila/opti-auth-portal/commit/eac274553bb3c0021f6c10f8d9d6ee4cab6ff4c9) |
| HU-20 | Account profile, security and preferences tabs | doing | [opti-auth-portal@014c8a9](https://github.com/code-corhuila/opti-auth-portal/commit/014c8a9384e1c781ddd689dbea3329448f613fa8) |
| HU-21 | Work order list and detail redesign | doing | [opti-sales-portal@a0903cb](https://github.com/code-corhuila/opti-sales-portal/commit/a0903cb5b144a7d1c1acc92226a035714b439ff9), [opti-sales-api@a17a24d](https://github.com/code-corhuila/opti-sales-api/commit/a17a24db519cdf86f07e992da8847f5773b82898) |
| HU-22 | Four-step new-sale wizard | doing | [opti-sales-portal@324a93f](https://github.com/code-corhuila/opti-sales-portal/commit/324a93f30fa9aad5ac3f67d35970d47c256a1f4b) |
| HU-23 | Optometry attention list and navigation | doing | [opti-customers-portal@26f8414](https://github.com/code-corhuila/opti-customers-portal/commit/26f8414d7a443dd4d0368e8d56942852a66752f7), [opti-front@08465c0](https://github.com/code-corhuila/opti-front/commit/08465c03c505c5cba90b8640fe3c22c0a2edc572) |
| HU-24 | Sales and order-status reports | doing | [opti-sales-api@73393a3](https://github.com/code-corhuila/opti-sales-api/commit/73393a36913f0bdcd697cfea243757b99b39a03d), [opti-sales-portal@d279aea](https://github.com/code-corhuila/opti-sales-portal/commit/d279aea44a4ad884e0d22e4a8b4eaad917a6a73c), [opti-front@bb7f498](https://github.com/code-corhuila/opti-front/commit/bb7f49877907d44fb2084331505c2149a530d159) |
| HU-25 | Sell lenses, accessories and liquids | doing | [opti-products-db@86b357d](https://github.com/code-corhuila/opti-products-db/commit/86b357d4b0d08a37ca6213a2689152ab1e2bed6b), [opti-products-api@06710dc](https://github.com/code-corhuila/opti-products-api/commit/06710dcdfd28aa4ecec54434eb1011f98aa1ef83), [opti-products-portal@46481c0](https://github.com/code-corhuila/opti-products-portal/commit/46481c00cf23847235a5b31b5bc559698dfa0f38), [opti-sales-api@6002dab](https://github.com/code-corhuila/opti-sales-api/commit/6002dabcdb5bb927ea68a6c1277756559f3b743a), [opti-sales-db@a92f443](https://github.com/code-corhuila/opti-sales-db/commit/a92f4436b9e56ff084c74cef43792253ea6e23ca), [opti-sales-portal@1c204f4](https://github.com/code-corhuila/opti-sales-portal/commit/1c204f4c89019b5b23c1de7436fe2ee33b473c3a), [opti-workflow@910cac3](https://github.com/code-corhuila/opti-workflow/commit/910cac3458af9362b0883aff55753ed7526d240b) |
| HU-26 | Electronic payment gateway authorization | doing | [opti-sales-api@6316944](https://github.com/code-corhuila/opti-sales-api/commit/6316944a8ed9c4f47dce359d96e761867dad0dd3), [opti-sales-api@8c7708d](https://github.com/code-corhuila/opti-sales-api/commit/8c7708d553fc52107c29dd2373b00e2ac11189c5), [opti-sales-db@7c7481a](https://github.com/code-corhuila/opti-sales-db/commit/7c7481a68be7218d044dc10d51dbb607a0261286) |
| HU-27 | Lens catalogue and stock reservation | doing | [opti-products-api@bd70122](https://github.com/code-corhuila/opti-products-api/commit/bd701227514571d607c190a38258b143ed400d61), [opti-products-api@fd18c37](https://github.com/code-corhuila/opti-products-api/commit/fd18c375d7a1fcfe20c1d4b29213bfd239f34e38), [opti-products-db@5877026](https://github.com/code-corhuila/opti-products-db/commit/5877026b8e9bad922eb9c8083a3d61bd4bc135fd), [opti-products-portal@8077273](https://github.com/code-corhuila/opti-products-portal/commit/80772735fcbcdb6734d5632f01bdc9c2fde2e504) |
| N/A | Study deliverables: persistence patterns and MVP 2 release | done | See section 6 and [import manifest](./import-manifest.md) |

Implementation is integrated in the code repositories; `doing` means acceptance/QA promotion is not verified here. This table includes team work. Bairon contributed HU-15, HU-16, HU-17, HU-18, HU-21 and the products portion of HU-25; HU-14 belongs to AllanZapata23, HU-24 to julianvargasb, and HU-13/19/20/22/23 and the remaining HU-25 work to JD, as recorded in the project handoff. HU-26/27 ownership awaits confirmation.

## 2. My individual contribution

- Registered team implementation evidence and the missing backlog stories. My feature contributions are the inventory summary, frame photos, patient summary and grouped form, work-order redesign, and the products part of HU-25. Team contributions are attributed above.
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

- The worker and workflow repositories contain Outbox dispatch and Saga implementation; acceptance, failure-path validation and QA/release promotion require separate evidence.
- Cut 1 monolith evidence does not establish completion of the integrated MVP 2 release.

## 4. Plan for next week

- Decide the message broker and record it as an ADR.
- Evaluate Saga (orchestration vs. choreography) for the work-order approval flow.
- Prepare the MVP 2 release: version tag, release notes and a rollback plan.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria
- [ ] Tests added/updated (unit / integration)
- [ ] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

Notes on unchecked items:
- Study documents have no HU branch or tests; the feature work above lives in the code repositories.

## 6. Evidence links

- Feature commits: see the table in section 1 (repositories under `code-corhuila`).
- Session summary: [`persistence-saga-outbox-cqrs.md`](./persistence-saga-outbox-cqrs.md)
- Session summary: [`release-shipping-mvp2.md`](./release-shipping-mvp2.md)
- Infographic: ![Persistence in Distributed Systems](./persistence-infographic.jpeg)
- Infographic: ![Release: Shipping an MVP of an Integrated System](./release-mvp-infographic.jpeg)

- Imported archive checksums: [import-manifest.md](./import-manifest.md).
- Backlog and acceptance criteria: [opti-docs branch](https://github.com/BackSua/opti-docs/tree/docs/backlog-week-09-10).
