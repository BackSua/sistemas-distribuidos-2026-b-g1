<!-- HU-STATUS TEMPLATE - do NOT remove the <!-- ... --> markers or the table headers.
     Your weekly grade is read AUTOMATICALLY from this file:
       03-week/hu-status/README.md  (inside YOUR fork). English. -->

# Weekly Status - Week 03

<!-- CONFIG-START - must match your profile repo (username/username) CONFIG -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Fork the `code-corhuila/opti-docs` governance framework into `BackSua/opti-docs`, configure it with `upstream` tracking, and adapt the entire `00-governance/` section (branch strategy, agile ceremonies, DoR/DoD, documentation rules, per-microservice docs standard, security policy and rules) to OptiView's real stack, backlog and academic cadence.

## 1. User stories worked this week

| HU ID | Title | Status (todo/doing/done) | Evidence (PR or commit URL) |
|---|---|---|---|
| HU-OPT-005 | Fork `code-corhuila/opti-docs` and configure `origin`/`upstream` | done | https://github.com/BackSua/opti-docs |
| HU-OPT-006 | Adapt `00-governance/git-conventions.md`, `agile-conventions.md`, `definition-of-done.md`, `definition-of-ready.md` and `README.md` to OptiView | done | https://github.com/BackSua/opti-docs/commit/543451a |
| HU-OPT-007 | Adapt `00-governance/documentation-rules.md`, `microservices-documentation.md`, `security-policy.md`, `security-rules.md` to OptiView | doing | Adapted locally, pending push by a teammate under their own account (see section 3) |

## 2. My individual contribution

- **Forked** [`code-corhuila/opti-docs`](https://github.com/code-corhuila/opti-docs) — the generic
  Microservices Governance Framework used as the documentation scaffold for the course — into
  [`BackSua/opti-docs`](https://github.com/BackSua/opti-docs), then cloned it locally and
  configured `origin` (my fork) and `upstream` (`code-corhuila/opti-docs`) so the relationship
  with the original template is preserved and future framework updates can still be pulled in
  without losing OptiView-specific work.
- Read the framework itself before touching any document: the root `README.md`
  ("How to use this framework"), `00-sdd-guide.md` (SDD methodology and weekly fill-in order),
  and the `00-governance/README.md` index, confirming `00-governance` wraps and applies to every
  other section.
- Read each of the 8 template files in `00-governance/` individually to understand what each one
  actually requires (branch strategy, sprint ceremonies, DoR/DoD checklists, documentation
  ownership, per-service doc standard, RBAC model, OWASP-aligned technical controls) before
  writing a single word of OptiView content — no template was filled from its filename alone.
- Gathered real OptiView project context instead of inventing it: the `ms-pacientes` product
  brief (`01-week/hu-status/prd.md`), the GitFlow evidence and HU-05/HU-06 issues from
  `02-week/hu-status/README.md`, the full HU-01…HU-12 backlog (60 story points across
  `ms-pacientes`, `ms-inventario`, `ms-ordenes`, `ms-facturacion`), and the real 5-member
  `TheIllusionists` GitHub organisation roster.
- **Rewrote `git-conventions.md`** around the GitFlow the team is actually running
  (`main ← qa ← develop ← hu-xxx-dev/qa` branches, matching the PRs already opened in Week 2),
  distinguishing the canonical product repo from the personal course-evidence fork, and set
  commit scopes to the real service names.
- **Rewrote `agile-conventions.md`** to replace the textbook Daily Scrum with a Weekly Sync
  anchored on the two real class sessions (Tuesday Planning, Wednesday Review/Retro), keeping
  "1 academic week ≈ 1 Sprint" explicit, and filled the estimation scale, backlog tool and
  team-velocity table with the project's actual numbers instead of bracketed placeholders.
- **Rewrote `definition-of-done.md` and `definition-of-ready.md`** so every checklist item
  references OptiView's real artifacts (hexagonal domain-layer rule from ADR-001, the `qa`
  branch as the academic staging equivalent, HU-05's actual acceptance criteria as a worked
  example) instead of generic bracketed text.
- After an initial pass, corrected the stack across all adapted files once the confirmed
  architecture was clarified: `ms-ordenes` and `ms-facturacion` run on **Go 1.22** (not Java),
  and there are **four** client apps — `portal-admin`/`portal-ventas` (Angular) and
  `portal-paciente`/`dashboard` (React) — not a single Angular portal.
- Verified there were no leftover generic placeholders (`[define]`, `[tool name]`, `[N]`, etc.)
  in any adapted file before treating it as complete, and marked the handful of genuinely
  undecided items (Tech Lead role assignment, Project board URL, secrets-vault choice) with an
  explicit `⚠️ Pendiente de definición por el equipo` instead of inventing an answer.
- Split the 8 adapted `00-governance` files with a teammate for authorship purposes: I pushed
  `README.md`, `git-conventions.md`, `agile-conventions.md`, `definition-of-done.md` and
  `definition-of-ready.md` under my own commit; `documentation-rules.md`,
  `microservices-documentation.md`, `security-policy.md` and `security-rules.md` were adapted
  and handed off for a teammate to push under theirs.

## 3. Blockers and risks

- `documentation-rules.md`, `microservices-documentation.md`, `security-policy.md` and
  `security-rules.md` are fully adapted but intentionally **not yet pushed** to
  `BackSua/opti-docs` — they are being handed to a teammate to commit under their own GitHub
  account so individual authorship is preserved across the team. HU-OPT-007 stays `doing` until
  that push lands.
- The GitHub CLI (`gh`) is not installed in the working environment and no GitHub token was
  configured, so the fork itself had to be created interactively through the browser (logged in
  via Chrome) rather than scripted — worth automating with `gh` before the next fork-heavy week.
- Several governance decisions are explicitly still open and marked `⚠️ Pendiente de definición
  por el equipo` rather than guessed: who holds the Tech Lead/PO role, the GitHub Projects board
  URL, the team chat channel, and the secrets-management tool for anything beyond local
  `docker-compose`. These need to be closed in an upcoming Tuesday Planning.

## 4. Plan for next week

- Confirm the teammate's push of the remaining 4 `00-governance` files and verify the full
  section is consistent (no stack contradictions, no stray placeholders) once merged.
- Create the GitHub Projects board referenced (but not yet created) in `agile-conventions.md`,
  with the `Story points` and `Microservicio` custom fields.
- Move on to `01-context/` and `02-domain/` per the SDD fill-in order in `00-sdd-guide.md`,
  reusing the bounded-context work already drafted in Week 1.
- Close the open questions listed in section 3 during Tuesday's Sprint Planning and update the
  `⚠️ Pendiente` markers in `00-governance/` with the team's actual decisions.

## 5. Compliance self-check

- [x] Conventional Commits - `type(scope): summary`
- [ ] Per-environment HU branch + PR to that environment (hu-xxx-dev -> develop, ...)
- [x] Testable acceptance criteria (governance docs were validated against the DoR/DoD checklist they themselves define)
- [ ] Tests added/updated (unit / integration) — documentation-only week, no service code changed
- [ ] DDD / hexagonal boundaries respected (domain has no I/O) — not applicable this week
- [x] No secrets; config via environment variables

Notes on unchecked items:
- Work landed directly on `main` in `opti-docs` (documentation scaffold setup), not through a
  per-environment HU branch — `opti-docs` does not run the product's `develop`/`qa`/`main`
  GitFlow, since it is documentation, not a deployable service.
- No test suite applies to Markdown governance documents; correctness was instead verified by
  re-reading each file against its own DoR/DoD checklist and grepping for leftover generic
  template placeholders.

## 6. Evidence links

- Fork with upstream configured: https://github.com/BackSua/opti-docs
- Original framework (upstream): https://github.com/code-corhuila/opti-docs
- Commit — adapted git/agile/DoD/DoR governance docs: https://github.com/BackSua/opti-docs/commit/543451a
- OptiView user stories source (HU-01 to HU-12): `02-week/hu-status/README.md` and `C:\Users\bairo\Downloads\OptiView_Historias_Usuario.md`
- Screenshot evidence of the pushed commit on GitHub:

![OptiView opti-docs governance commit evidence — commit 543451a on BackSua/opti-docs, 5 files changed (README.md, agile-conventions.md, definition-of-done.md, definition-of-ready.md, git-conventions.md)](./opti-docs-governance-commit-evidence.png)
