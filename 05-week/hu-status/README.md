<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       05-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 05

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Ship OptiView MVP 1 (Corte 1) - a versioned, running increment: a containerized service (DDD + hexagonal / clean architecture) with the walking skeleton for HU-01, HU-05 and HU-08, promoted to main and tagged v1.0.0.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| HU-01 | Register a patient (unique document) | done | https://github.com/jdtovar07/optiview-platform/commit/c45859f9279c75dc2e7dd6427dfb953704b2acb9 |
| HU-05 | Register a frame in inventory (unique SKU, stock >= 0, sale_price >= cost) | done | https://github.com/jdtovar07/optiview-platform/commit/c45859f9279c75dc2e7dd6427dfb953704b2acb9 |
| HU-08 | Create a work order - happy path (link patient + frame with stock, status QUOTATION, deduct stock) | done | https://github.com/jdtovar07/optiview-platform/tree/feature/mvp1-corte1 |

## 2. My individual contribution
- Built the **backend** of the platform as a modular monolith with **clean architecture** (Bancolombia scaffold layout, single Maven module): `model` = pure domain objects + value objects (`Money`, `Sku`, `Document`, `WorkOrderNumber`) + gateway ports, with no framework or I/O; `usecase` = framework-free use cases wired as `@Bean`; `infrastructure` = driven-adapters (JPA `*Data` entities + mappers + adapters, BCrypt, JWT) and entry-points (REST controllers + DTOs + error handler).
- **HU-01**: register patient rejecting a duplicate document number; search by name/document; detail endpoint.
- **HU-05**: register frame enforcing unique SKU, `stock >= 0` and `sale_price >= cost` (domain + DB CHECK constraints).
- **HU-08 (happy path)**: `CreateWorkOrderUseCase` links an existing patient and a frame with stock; order created as `QUOTATION`, total = frame sale price, one stock unit deducted - all inside a single unit of work via the `TransactionRunner` port; rejects with `insufficient stock` when the frame has none.
- **Auth**: `users` table (BCrypt), `POST /api/auth/login` returning a signed JWT, Spring Security stateless, role-based authorization (`ADMIN` / `SELLER` / `OPTOMETRIST`). `PasswordHasher` and `TokenProvider` modelled as domain ports, implemented in infrastructure.
- **Persistence & containers**: PostgreSQL 16 + Flyway `V1__init.sql`; backend `Dockerfile` (Maven build -> JRE); `docker-compose` (db + backend). Verified end-to-end with `docker compose up` (401/403, duplicate document, duplicate SKU, price < cost, HU-08 happy path + `insufficient stock`).
- **Tests**: `model/FrameTest`, `model/PatientTest`, `usecase/CreateWorkOrderUseCaseTest` (no Spring, no containers) + `OptiviewIntegrationTest` (`@SpringBootTest` + Testcontainers PostgreSQL, skipped when Docker is absent).
- **Docs & release**: ADR 0001-0004 and `docs/modelo-datos.md`; `CHANGELOG.md`; promoted `feature/mvp1-corte1` -> `develop` -> `qa` -> `main`; **tag `v1.0.0` on `main`**.

## 3. Blockers and risks
- Promotion `feature -> develop -> qa -> main` was done through merge commits; the formal GitHub PRs for each environment still need to be opened if the review record is required.
- Work-order state progression (`IN_LAB` / `READY` / `DELIVERED`) and the full HU-08 (lenses, treatments, prescription validation) are out of scope for Corte 1.

## 4. Plan for next week
- Open the formal per-environment PRs (`hu-xxx-dev -> develop`, `-> qa`, `-> main`).
- Implement work-order state transitions and start the full HU-08.
- Add a CI workflow that runs `mvn test` on every PR.

## 5. Compliance self-check
- [x] Conventional Commits - `type(scope): summary`
- [x] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...) - `feature/mvp1-corte1` -> `develop` -> `qa` -> `main` (promoted via merges; formal PRs pending)
- [x] Testable acceptance criteria
- [x] Tests added/updated (unit / integration)
- [x] DDD / hexagonal boundaries respected (domain has no I/O)
- [x] No secrets; config via environment variables

## 6. Evidence links
- Platform repository: https://github.com/jdtovar07/optiview-platform
- Backend feature branch: https://github.com/jdtovar07/optiview-platform/tree/feature/mvp1-corte1
- Backend feature commit: https://github.com/jdtovar07/optiview-platform/commit/c45859f9279c75dc2e7dd6427dfb953704b2acb9
- Release tag v1.0.0 on main: https://github.com/jdtovar07/optiview-platform/releases/tag/v1.0.0
- Architecture decisions (ADR): https://github.com/jdtovar07/optiview-platform/tree/main/docs/adr
- Data model: https://github.com/jdtovar07/optiview-platform/blob/main/docs/modelo-datos.md
