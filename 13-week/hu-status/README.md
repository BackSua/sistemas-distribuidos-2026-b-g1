<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       13-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 13

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Understand security in distributed systems (authentication, authorization, zero trust) and how threat modeling turns risks into a security backlog; prepare the Corte 3 certification.
<!-- CONFIG-END -->

## 1. User stories worked this week
| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| N/A | W13 workshop: workshop start and boundary claim (CP0) | done | [b986354](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/b986354b8adb1ed2c03021ada8093ad270ea21cb), [issue 30](https://github.com/code-corhuila/opti-docs/issues/30) |
| N/A | W13 workshop: attack surface map and trust boundaries (CP1) | done | [74c3c60](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/74c3c60e7dc1cee2b254ff550058c95a862de503) |
| N/A | W13 workshop: STRIDE of opti-api-gateway to opti-products-api (CP2) | done | [PR 31](https://github.com/code-corhuila/opti-docs/pull/31) (closed), [library record](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/1abf7992f45b92c4be05e5db339d64bb8714291b) |
| N/A | W13 workshop: security backlog, three stories (CP3) | done | [issue 38](https://github.com/code-corhuila/opti-docs/issues/38), [issue 39](https://github.com/code-corhuila/opti-docs/issues/39), [issue 40](https://github.com/code-corhuila/opti-docs/issues/40), [31a3da5](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/31a3da5c2077729fc26cb0e4c66bdfbf93ac81f9) |
| N/A | W13 workshop: security gates and negative authorization test (CP4) | done | [9fd708d](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/9fd708db6ec61a82ac234847e8ee67cdaf689ed3), [03e6b7b](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/03e6b7b69b2d797bbc7eb53adf2fe8cb16e9e7d3) |
| N/A | W13 workshop: peer review of a teammate's STRIDE (CP5) | done | [review on PR 32](https://github.com/code-corhuila/opti-docs/pull/32#pullrequestreview-5691551540) |
| N/A | W13 final: STRIDE reviewed and finished, follow-up pull request (CP6) | done | [3b2499d](https://github.com/BackSua/opti-docs/commit/3b2499d), [PR 52](https://github.com/code-corhuila/opti-docs/pull/52) |
| N/A | W13 final: security backlog refined, related stories linked (CP7) | done | [46e9ecd](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/46e9ecd4aea5c38d9ce5c2218778e4aed002bf84), [issue 38](https://github.com/code-corhuila/opti-docs/issues/38), [issue 39](https://github.com/code-corhuila/opti-docs/issues/39), [issue 40](https://github.com/code-corhuila/opti-docs/issues/40) |
| N/A | W13 final: negative test run and workflow validated (CP8) | done | [a2237cf](https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/a2237cf1c4c0f7514d19fa1368a43b1d910f7c41) |
| N/A | W13 final: implementation plan in the reflection document (CP9) | done | [reflection document](https://docs.google.com/document/d/1ZZ_DIh3lNjSBk9JnYazywtwFcThDl1aeMGu_fksqZ80/edit) |
| N/A | Study deliverables: authN, authZ and zero trust; threat modeling and the security backlog | done | See section 6 |
| N/A | Corte 3 certification (Huawei HCCDP-Solution Architectures): base structure synced into the fork, course not started yet | todo | [`certification/`](../certification/README.md) |

## 2. My individual contribution

- Studied authentication against authorization, tokens (JWT, OAuth2, OIDC), the API gateway as the
  single point that validates tokens, service-to-service trust (mTLS, short-lived tokens, workload
  identity), zero trust and common vulnerability classes. Summary and infographic are in this folder.
- Studied threat modeling: the four key questions, data-flow diagrams and trust boundaries, STRIDE,
  risk rating, the four responses to a risk and how mitigations become security backlog stories.
  Summary and infographic are in this folder.
- Relevance to OptiView: `api-gateway` is meant to validate the JWT issued by Keycloak and forward
  `X-User-Id` and `X-User-Role`, while each service checks the role in its use case layer; patient
  data (document, optical formula) is the asset to protect first.
- Synced the `certification/` base structure from the course repository into the fork (only that
  folder, to avoid importing other students' work) and read the certification guide and the
  week 13 slides.

- Workshop on threat modeling (delivery 1): claimed the boundary `opti-api-gateway` to
  `opti-products-api`, drew the attack surface with five trust boundaries, wrote the STRIDE table in
  a pull request of the team docs repository, opened three security stories (issues 38 to 40) with
  labels `security` and `P0`/`P1`, wrote a security workflow and a negative authorization test that
  runs the real `RequestRules.requireRole` rule of `opti-products-api`, and reviewed a teammate's
  STRIDE. The library book is Tanenbaum and Van Steen (2008), call number 005.4476 T15d, record at
  19:36; the reflection document is linked in `13-week/workshop/README.md`.

- Workshop final delivery: answered the review round against the real code (service tokens live up to 90
  days with no revocation, found in `opti-auth-api`), rewrote the criteria of issues 38 to 40 so each one can
  fail in a test and linked them with issues 47 and 48 of teammates, ran the negative test again
  (2 tests successful, 0 failed) and validated `security.yml`, and wrote the implementation plan in the
  reflection document.

## 3. Blockers and risks

- The certification needs the teacher's class invitation, expected by 2026-10-28; the course
  (56 hours) must be completed with real evidence before 2026-11-15, so the study hours have to
  start this week.
- The three security issues have to be added to the team GitHub Project board by hand, because the
  token I use from the command line cannot write to Projects.
- The first STRIDE pull request (31) was merged by mistake before a teammate reviewed it, against rule 8, and
  nobody left a review on it. The follow-up pull request 52 stays open and I do not merge it.
- `actionlint` is not installed on my machine; `security.yml` was validated with a YAML parser only.

## 4. Plan for next week

- Join the Huawei class when the invitation is published and start the pre-class guidance and
  chapters 1 to 6, with dated screenshots.
- Draft a first threat model of `place-order` and of the payment authorization with STRIDE.
- Start story 38 (shorter service tokens) in `opti-auth-api` once the team agrees on the sprint.

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

- Session summary: [`security-authn-authz-zero-trust.md`](./security-authn-authz-zero-trust.md)
- Session summary: [`threat-modeling-security-backlog.md`](./threat-modeling-security-backlog.md)
- Infographic: ![Security in Distributed Systems](./security-authn-authz-zero-trust-infographic.jpeg)
- Infographic: ![Threat Modeling and the Security Backlog](./threat-modeling-security-backlog-infographic.jpeg)
- Certification folder: [`certification/`](../certification/README.md)
