# Week 14 — Session 2: Your Pipeline, DoD-as-Gates and Branch Protection

**Topic:** Planning your delivery pipeline
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

Knowing CI/CD concepts (Session 1) is not enough; you must design **your own pipeline**, turn your **Definition of Done** into automated gates, and protect the `main` branch so quality is enforced by the machine. The guiding idea: *make 'Definition of Done' automatic and the main branch always releasable.*

## Design Your Pipeline

Define the concrete stages for **your** project and what each one runs:

- **Build** — compile code, install dependencies, run linters.
- **Test** — run unit, integration and end-to-end tests; collect coverage.
- **Scan** — run SAST and dependency/vulnerability scans, check licenses.
- **Package** — build the artifact (e.g. a container image), version and sign it.
- **Deploy to staging** — deploy to a staging environment, run smoke tests.
- **Deploy to production** — release (with approvals if needed), run post-deploy checks, monitor.

## Definition of Done (DoD)

The **Definition of Done** is the shared checklist a change must meet to count as "done":

- Code reviewed by at least one qualified reviewer.
- Tests written and passing (unit and integration).
- Documentation updated (README, API docs, ADRs).
- No known security issues (no critical/high findings in code or dependencies).
- Acceptance criteria met.

A shared DoD prevents "done means different things to different people".

## DoD as Gates

Turn each DoD item into an **automated check** in the pipeline, so "done" is enforced by the machine, not by opinion:

- DoD "code reviewed" → **automated check:** PR requires a review.
- DoD "tests passing" → **automated check:** tests run and pass.
- DoD "docs updated" → **automated check:** docs updated (or a check for changed areas).
- DoD "no security issues" → **automated check:** security scan passes (SAST, dependency).
- DoD "acceptance criteria met" → **automated check:** acceptance/e2e tests pass.

A change **cannot merge or deploy until all the gates pass**.

## Branch Protection Rules (on main)

Protect the `main` branch with rules such as:

- **Require pull requests** — all changes go through a PR, no direct commits.
- **Require passing status checks** — all required checks must be green.
- **Require at least one review** — from a code owner where possible.
- **Require up-to-date branches** — the PR must be current with `main`.
- **Block force-push and direct commits** — history is protected.

## Required Status Checks

Wire the exact checks that must be green to merge:

- **Build** — project builds successfully.
- **Lint** — code passes linting (e.g. ESLint, flake8).
- **Unit tests** — all unit tests pass.
- **Integration tests** — all integration tests pass.
- **Coverage** — meets the minimum coverage threshold (e.g. ≥ 80%).
- **SAST / dependency scan** — no critical or high issues in code or dependencies.

## Example Flow

A change moves from pull request to production like this:

**Open PR** → **checks run** (pipeline runs required checks) → **review** (a reviewer approves) → **all gates green** → **merge to main** → **pipeline deploys** (CI/CD deploys from `main`).

## Key Principle

**If the gate is not automated, the Definition of Done is just a wish.** A DoD that depends on people remembering to check things will drift; gates wired into branch protection and the pipeline make it real.

## Key Takeaways

- Design concrete pipeline stages for your project and what each runs.
- Write a shared **Definition of Done** and turn each item into an **automated gate**.
- Protect `main` with **branch protection rules** and **required status checks**.
- Follow the flow: **open PR → checks → review → gates green → merge → deploy**.
- An un-automated gate is only a wish.

## Infographic

See the Pipeline / DoD-as-gates / branch-protection infographics (vertical and horizontal) in this folder for a visual summary.
