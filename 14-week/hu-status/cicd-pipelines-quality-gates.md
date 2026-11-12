# Week 14 — Session 1: CI/CD — Pipelines and Quality Gates

**Topic:** Continuous Integration and Continuous Delivery
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

Continuous Integration and Continuous Delivery are what let a distributed team ship **many small changes safely**. This session covers the difference between CI and CD, the **pipeline stages**, the **quality gates** that make "done" mechanical instead of an opinion, **trunk-based flow** with pull-request checks, and the **DORA metrics** that tell you whether your delivery is actually healthy. The guiding idea: *ship many small changes safely, automatically.*

## CI vs CD vs Continuous Deployment

- **Continuous Integration (CI):** developers merge and test every change often (ideally many times a day), so integration problems are found early.
- **Continuous Delivery (CD):** the application is kept **always releasable** — every change that passes the pipeline is ready to deploy, but the final push to production is a manual decision.
- **Continuous Deployment:** goes one step further — every change that passes all gates is **released to production automatically**, with no manual step.

## Why It Matters

A distributed team ships many small changes to many services. Automating integration, testing and release makes those releases **frequent, fast and low-risk**. Small, automated changes are easier to review, test and roll back than big, infrequent ones.

## The Pipeline Stages

A typical CI/CD pipeline runs these stages in order:

1. **Commit** — code is pushed to the repository.
2. **Build** — compile and resolve dependencies.
3. **Test** — run unit, integration and end-to-end tests.
4. **Package** — build an immutable artifact (e.g. a container image).
5. **Deploy (staging)** — deploy to a staging environment and run smoke tests.
6. **Release (production)** — release to production (manually in Continuous Delivery, automatically in Continuous Deployment).

## Quality Gates

**Quality gates** are automated checks that must pass before a change can move to the next stage:

- Tests green (unit, integration, e2e).
- Code coverage above a threshold.
- Linting and formatting pass.
- Security scan passes (SAST and dependency scan).
- Build succeeds.

Gates make **"done" mechanical, not an opinion**: a change advances only when the machine confirms every gate is satisfied.

## Trunk-Based Flow with PR Checks

Modern teams favor **trunk-based development**:

- Short-lived branches (hours or days, not weeks).
- Small pull requests that are easy to review.
- Required status checks that must be green before merge.
- At least one code review before merging to `main`.

This keeps `main` healthy and always close to releasable.

## DORA Metrics

The **DORA metrics** (from the DevOps Research and Assessment program) measure how healthy delivery really is:

- **Deployment Frequency** — how often you deploy to production.
- **Lead Time for Changes** — time from commit to running in production.
- **Change Failure Rate** — percentage of deployments that cause a failure (incident, rollback).
- **Time to Restore Service** — how long it takes to recover from a failed change.

High performers deploy often, with short lead times, low failure rates and fast recovery.

## Key Principle

**If it is not automated and gated, it is not really done.** Relying on people to remember checks is unreliable; encoding them as pipeline gates makes quality consistent and repeatable.

## Key Takeaways

- **CI** integrates and tests often; **CD** keeps the app releasable; **Continuous Deployment** releases automatically.
- The pipeline runs **Commit → Build → Test → Package → Deploy → Release**.
- **Quality gates** make "done" mechanical.
- **Trunk-based flow** with required PR checks keeps `main` healthy.
- **DORA metrics** measure delivery health: frequency, lead time, failure rate, restore time.

## Infographic

See the CI/CD infographics (vertical and horizontal) in this folder for a visual summary.
