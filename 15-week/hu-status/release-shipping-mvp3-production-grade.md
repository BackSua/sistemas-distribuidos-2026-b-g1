# Week 15 — Session 2: Shipping the MVP 3 — A Production-Grade Release

**Topic:** Releasing a production-grade version
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

Testing proves the system *can* survive production (Session 1). This session is about actually **releasing it** — turning the third and final MVP into a **production-grade** version that real users depend on. Production-grade is not "it runs on my machine" or even "it passes the demo"; it means the system is observable, operable, secure and recoverable, with a plan for the day something goes wrong. The guiding idea: *shipping is the start of operating, not the finish line.*

## What "Production-Grade" Means

A production-grade release is one you can trust in front of real users and keep running at 2 a.m. without the author present. It is defined by qualities the demo version never needed:

- **Reliable** — meets its availability and latency targets, and degrades gracefully when a dependency fails.
- **Observable** — you can see what it is doing through metrics, logs and traces, not by guessing.
- **Operable** — someone other than the author can deploy it, monitor it, and recover it using written procedures.
- **Secure** — authentication, authorization, secrets handling and dependency scanning are in place.
- **Recoverable** — there is a tested way back (rollback, backups, reversible migrations) if the release goes wrong.

## The MVP 1 → 2 → 3 Progression

The three MVPs are not three sizes of the same thing; they raise the bar each time:

- **MVP 1 — "it works":** the core feature exists and runs. Proves the idea.
- **MVP 2 — "it works together":** the services integrate, the main flows are complete, and it survives basic testing. Proves the system.
- **MVP 3 — "it works in production":** it is reliable, observable, operable and secure, with a release and rollback plan. Proves it is ready for real users.

Each step adds the engineering rigor the previous one deliberately skipped. MVP 3 is where that debt is paid.

## The Production-Readiness Checklist

Before calling a release production-grade, confirm each item — this is the gate between "feature-complete" and "ready to ship":

- **CI/CD gates green** — build, tests (unit, integration, contract, E2E) and security scans all pass; `main` is releasable.
- **Observability in place** — metrics, structured logs and distributed tracing are wired, with dashboards and alerts.
- **Security done** — authN/authZ enforced, secrets in a vault (not in code), dependencies scanned, no critical findings.
- **Load tested** — the system meets its latency and throughput targets at expected peak, with headroom.
- **Config & secrets ready** — environment config and secrets exist and are correct for production.
- **Database migrations planned** — written, tested, and ideally reversible/backward-compatible.
- **Backups verified** — recent backups of data and critical state exist and have been test-restored.
- **Rollback plan ready** — versioned artifacts and a clear, pre-agreed trigger to roll back.
- **Runbooks written** — how to deploy, how to roll back, and what to do for the top failure modes.

## The Release Steps

With the checklist satisfied, the release itself follows a disciplined sequence:

1. **Tag the version** — create an immutable, versioned release artifact (e.g. a signed container image at `v3.0.0`).
2. **Deploy with zero downtime** — use a safe strategy (blue-green, canary, or rolling) so users are never cut off. A **canary** sends a small slice of traffic to the new version first.
3. **Run smoke and health checks** — automated checks confirm the new version is up, healthy and serving correctly before it takes full traffic.
4. **Monitor the golden signals** — watch **latency, traffic, errors and saturation** closely during and right after the release.
5. **Decide: keep or roll back** — if the signals stay healthy, promote the release to 100%; if they breach the trigger, **roll back immediately** to the last good version. Rolling back is a normal, planned outcome, not a failure of character.

## Operability — Life After the Release

Production-grade means the system can be *run*, not just shipped. Operability is what makes it survivable long after launch:

- **Runbooks** — step-by-step procedures for deploying, rolling back, and handling common incidents.
- **On-call** — a clear owner is reachable when alerts fire, with an escalation path.
- **SLOs and error budgets** — agreed **Service Level Objectives** (e.g. 99.9% availability, p95 latency < 300 ms) and an **error budget** that decides when to slow down and stabilize instead of shipping more features.
- **Dashboards and alerting** — alerts fire on symptoms users feel (error rate, latency) rather than on noise, so on-call is woken for real problems only.
- **Incident response & postmortems** — a way to detect, mitigate and learn from incidents, blamelessly, so the same failure does not recur.

## Definition of Done for a Production-Grade Release

A release is "done" only when all of the following are true — not merely when the feature works:

- Code reviewed, all tests and security scans green.
- Observability (metrics, logs, traces), dashboards and alerts live.
- Load tested against production-like traffic.
- Rollback plan and runbooks written and verified.
- SLOs defined and an owner/on-call assigned.
- Deployed with a zero-downtime strategy and confirmed healthy in production.

## Key Principle

**Shipping is the start of operating, not the finish line.** The moment of release is not the end of the work — it is the beginning of the system's real life, where it must be watched, understood, kept within its SLOs, and recovered when it fails. A production-grade MVP 3 is one built to be *operated*, not just one that was successfully deployed once.

## Key Takeaways

- **Production-grade** = reliable, observable, operable, secure and recoverable — far beyond "it works."
- The **MVP 1 → 2 → 3** path raises the bar each step; MVP 3 adds the production rigor.
- Pass a **production-readiness checklist** before shipping: CI/CD, observability, security, load, migrations, backups, rollback, runbooks.
- Release in steps: **tag → zero-downtime deploy → smoke/health checks → monitor golden signals → keep or roll back**.
- **Operability** (runbooks, on-call, SLOs/error budgets, dashboards, postmortems) is what keeps it alive after launch.
- A release is **done** only when it is observable, load-tested, recoverable and owned — not just deployed.

## Infographic

See the Shipping-the-MVP-3 infographics (vertical and horizontal) in this folder for a visual summary.
