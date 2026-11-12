# Week 15 — Session 1: Testing at Scale — Contract, E2E, Load and Chaos

**Topic:** Testing distributed systems
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

In a distributed system the hard bugs do not live inside one service — they live **between** services: in the network, in the contracts two teams agreed on months ago, under load, and in the failures nobody planned for. Passing unit tests proves each piece works in isolation; it does not prove the *system* works. This session covers the layers of testing a distributed system actually needs — the **testing pyramid** adapted to many services, **contract tests**, **integration tests** with real dependencies, **end-to-end (E2E) tests**, **load testing**, and **chaos engineering**. The guiding idea: *prove the distributed system survives production before users find the limits.*

## Why Unit Tests Are Not Enough

Unit tests are fast, cheap and essential — but they mock everything around the unit. That means they pass even when:

- A service calls another with a payload the other side no longer accepts.
- The network is slow, drops packets, or times out.
- Two services are each correct but disagree about a shared format or an ordering assumption.
- The system behaves fine with one user but collapses with ten thousand.
- A dependency is down and the caller has no fallback.

A green unit-test suite tells you the code is correct against *your assumptions*. Distributed failures come from the assumptions being wrong. You have to test the **interactions**, not just the components.

## The Testing Pyramid for Distributed Systems

The classic testing pyramid still holds, but each layer takes on a distributed meaning:

- **Unit tests (base, many, fast):** one function or class in isolation, dependencies mocked. Milliseconds. Run on every commit.
- **Integration / contract tests (middle):** a service against a real dependency (database, queue) or against the *contract* of a service it calls. Seconds. Run in CI.
- **End-to-end tests (top, few, slow):** a real user journey across several running services. Minutes. Run before release.
- **Load and chaos (alongside the top):** not about correctness of a single path but about behavior under stress and failure.

Keep many fast tests at the bottom and few slow ones at the top. An "ice-cream cone" (mostly slow E2E tests, few unit tests) is slow, flaky and expensive to maintain.

## Contract Tests

When service A calls service B, they share an implicit **contract**: the request shape A sends and the response shape B returns. If B changes its response and A is not updated, the system breaks — even though B's own tests still pass.

**Contract testing** (e.g. with **Pact**, in a *consumer-driven* style) makes that contract explicit and checked automatically:

- The **consumer** (A) declares exactly what it sends and what it expects back.
- That expectation becomes a **contract** that the **provider** (B) must verify against in its own pipeline.
- If B changes in a way that breaks the contract, **B's build fails** — before it is ever deployed.

This catches integration breakages **without having to run both services together**, which is fast and reliable. It is the single most valuable test type for teams that own different services.

## Integration Tests with Real Dependencies

Contract tests check the *shape* of an interaction; integration tests check that a service actually works against a **real** dependency rather than a mock:

- A real database (often spun up in a container, e.g. with **Testcontainers**) to catch schema, query and transaction problems.
- A real message queue to verify publish/consume behavior and serialization.
- A real cache, search engine or third-party sandbox.

These run slower than unit tests but catch a whole class of bugs — wrong SQL, bad migrations, serialization mismatches — that mocks hide by definition. The rule of thumb: mock what you don't own, use the real thing for what you do.

## End-to-End (E2E) Tests

E2E tests exercise a **complete user journey** through the whole running system — multiple services, the real network, real data stores — the way a user actually experiences it. For example: *register → log in → place an order → receive confirmation*, hitting every service in the chain.

E2E tests give the highest confidence that the system works as a whole, but they are:

- **Slow** (minutes), because real services and real I/O are involved.
- **Flaky**, because any service, any network blip, or any timing issue can fail them.
- **Expensive** to maintain.

So keep them **few and focused on critical paths** only. They are the final proof before release, not the place to test every edge case — push those down to contract and unit tests.

## Load Testing

Functional tests answer *"does it work?"*. Load testing answers *"does it still work under realistic and peak traffic?"* It reveals how the system behaves as concurrency rises — where latency climbs, where throughput plateaus, where it breaks.

Common tools: **k6**, **JMeter**, **Gatling**, **Locust**.

Kinds of load test:

- **Load test** — expected normal and peak traffic; verify latency and throughput stay within targets (SLOs).
- **Stress test** — push past the expected peak to find the breaking point and see how it fails.
- **Soak / endurance test** — sustained load over hours to expose memory leaks and resource exhaustion.
- **Spike test** — a sudden surge, to check autoscaling and back-pressure.

Watch the **golden signals** while testing: latency, traffic, errors and saturation. The goal is to know your limits *before* real users find them.

## Chaos Engineering

In a distributed system, failure is not an exception — it is the normal state at scale. Chaos engineering is the practice of **deliberately injecting failure** into a system (ideally in production or a production-like environment) to prove it is resilient:

- **Kill** a service instance and confirm traffic reroutes and the system stays up.
- **Add latency** or packet loss to a network path and verify timeouts, retries and fallbacks behave.
- **Exhaust** CPU, memory or disk on a node and check graceful degradation.
- **Break** a dependency and confirm circuit breakers trip and the blast radius stays contained.

The method: form a **hypothesis** ("if payment-service dies, checkout degrades gracefully and recovers"), run a small controlled **experiment**, measure, and fix what you find. Tools include **Chaos Monkey** / the Simian Army, **Gremlin**, and **LitmusChaos**. Chaos engineering turns "we think it's resilient" into "we have evidence it is."

## Key Principle

**Test the system, not just the code.** Every service passing its own unit tests tells you nothing about whether they work *together*, survive load, or recover from failure. Confidence in a distributed system comes from testing the interactions, the contracts, the limits and the failure modes — deliberately, and before your users do it for you.

## Key Takeaways

- **Unit tests aren't enough** — distributed bugs live between services, under load, and in failures.
- Keep the **testing pyramid**: many fast unit tests, fewer integration/contract tests, very few E2E tests.
- **Contract tests (Pact, consumer-driven)** catch integration breakages early, without running both services together.
- **Integration tests** use real dependencies (DB, queue) via containers to catch what mocks hide.
- **E2E tests** prove critical user journeys end to end — keep them few and focused.
- **Load testing** (k6, JMeter, Gatling) finds latency and throughput limits before users do.
- **Chaos engineering** injects failure deliberately to prove resilience and contain the blast radius.

## Infographic

See the Testing-at-scale infographics (vertical and horizontal) in this folder for a visual summary.
