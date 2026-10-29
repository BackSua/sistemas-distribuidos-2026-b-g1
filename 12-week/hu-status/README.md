<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       12-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 12

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Understand how a containerized distributed system reaches real users safely: cloud service models, zero-downtime strategies, deployment plans, infrastructure as code and rollback.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | Study deliverables: cloud deployment models and strategies; deployment plan, IaC and rollback | done | See section 6 |

## 2. My individual contribution

- Studied cloud deployment: IaaS, PaaS, containers as a service and serverless; the path from a
  container image to a running service (registry, orchestrator, load balancer, health checks);
  rolling, blue-green and canary releases; horizontal and vertical scaling and autoscaling.
  Summary and infographic are in this folder.
- Studied the deployment plan, infrastructure as code (declarative against imperative), the
  pre-deployment checklist and the rollback plan. Summary and infographic are in this folder.
- Relevance to OptiView: every service is already a container started with Docker Compose from
  `opti-infra`, and the gateway reaches each service by name. The deployment plan and rollback
  ideas apply to the Compose files and the image tags published to GHCR by the CI of each repository.

## 3. Blockers and risks

- OptiView has no rollout strategy decided yet (rolling, blue-green or canary); it is recorded as a
  pending team decision.
- Database migrations of the `-db` repositories must be backward compatible before any rollback
  of a service is safe.

## 4. Plan for next week

- Propose a rollback checklist per service (previous image tag, compatible migration, health check).
- Start the Corte 3 certification: join the Huawei class once the teacher publishes the invitation
  (announcement due by 2026-10-28).

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

- Session summary: [`cloud-deployment-models-strategies.md`](./cloud-deployment-models-strategies.md)
- Session summary: [`deployment-plan-iac-rollback.md`](./deployment-plan-iac-rollback.md)
- Infographic: ![Cloud Deployment](./cloud-deployment-infographic.jpeg)
- Infographic: ![Deployment Plan, IaC and Rollback](./deployment-plan-iac-rollback-infographic.jpeg)
