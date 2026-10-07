# Week 10 (Session 2) — Release: Shipping MVP 2 (Integrated System)

**Topic:** Release — shipping MVP 2 as one integrated system
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

After building the persistence patterns (Session 1), Session 2 is about **delivering the product**: packaging all the independent services into a single integrated system and shipping **MVP 2** to users. The goal is to turn separate services into one working release that people can actually use.

## 1. What is a Release?

**What it is:** A packaged, versioned build of the system that is delivered to users.

**Why it matters:** A clear release gives everyone a known, stable version to install, test and support.

**Example:** Tagging the system as `v1.0.0` and publishing it so users can run that exact version.

## 2. MVP (Minimum Viable Product)

**What it is:** The smallest version of the product that delivers real value and can be tested with users.

**Why it matters:** It lets you learn from real feedback early, without building every feature first.

**Example:** MVP 2 lets a user place an order end to end, even if advanced features (discounts, reports) are not ready yet.

## 3. Integrated System

**What it is:** All the independent services working together as one single product.

**Why it matters:** Services that pass their own tests can still fail together; integration proves the whole flow works.

**Example:** The User Service, Order Service and Payment Service connect so a complete purchase works from start to finish.

## 4. Release Steps

1. **Integrate services** — connect the services so they work as one system.
2. **Run end-to-end tests** — check the full user flow across all services.
3. **Tag a version** — mark the exact code of this release (for example, `v1.0.0`).
4. **Deploy** — publish the release to the target environment.
5. **Verify** — confirm the deployed system behaves as expected.

## 5. Good Practices

- **Semantic versioning** — use clear version numbers like `MAJOR.MINOR.PATCH` (e.g. `v1.2.3`).
- **Release notes** — write down what changed, so users and teammates know what is new.
- **Rollback plan** — prepare a way to return to the previous version if the release fails.

## Infographic

![Release: Shipping an MVP of an Integrated System](./release-mvp-infographic.jpeg)
