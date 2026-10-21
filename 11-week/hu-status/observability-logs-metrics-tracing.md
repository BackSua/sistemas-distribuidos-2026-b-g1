# Week 11 — Session 1: Observability — Logs, Metrics and Distributed Tracing

**Topic:** Observability in distributed systems
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

When a single user request crosses five or more services, "just check the logs" is no longer enough to understand what happened. **Observability** is the practice of instrumenting a system so that, from its external outputs alone, you can understand its internal state — and answer *what is happening and why* in production, quickly. This guide explains the core concepts: the three pillars (logs, metrics and traces), correlation IDs, the golden signals, and how observability differs from traditional monitoring.

## What is Observability?

Observability is the ability to understand a system's internal state by looking only at the data it emits: its logs, metrics and traces. A system is "observable" when you can ask new questions about its behavior — including questions you did not anticipate — without shipping new code to expose more data.

This matters because distributed systems fail in complex, unexpected ways. A request may touch many services, and the root cause of a problem is often far from where the symptom appears. Good observability lets engineers move from "something is wrong" to "here is exactly where and why" in minutes instead of hours.

## The Three Pillars of Observability

### 1. Structured Logs

Logs are timestamped records of discrete events ("an order was created", "a payment failed"). **Structured logs** are written in a machine-readable format (usually JSON) with explicit fields — `timestamp`, `level`, `service`, `message`, `trace_id`, `user_id`, and so on — instead of free-form text.

- **Why it matters:** structured fields can be searched, filtered and aggregated, which makes investigating a specific event or error far faster.
- **Example:** an error log such as `{ "level": "ERROR", "service": "payment-service", "message": "Payment failed", "trace_id": "4f61a3b9", "user_id": "u-1234" }` tells you exactly which service, user and request were involved.
- **Trade-off:** logs are detailed but expensive to store at high volume, so they are best for investigation rather than for always-on dashboards.

### 2. Metrics

Metrics are numeric measurements aggregated over time. The common types are **counters** (values that only go up, like total requests), **gauges** (values that go up and down, like memory usage) and **histograms** (distributions, like request latency).

- **Why it matters:** metrics are cheap to store and fast to query, which makes them ideal for dashboards and alerts.
- **Examples:** request rate (requests/second), error rate (% of failed requests), and p95 latency (the latency below which 95% of requests complete).
- **Limitation:** metrics tell you *that* something changed, but not the detail of any single request.

### 3. Distributed Tracing

A **trace** follows one request as it travels across every service. Each step is a **span** with a start time and a duration; together the spans form a timeline of the whole request.

- **Why it matters:** tracing reveals *where* time is spent and *where* a request failed across service boundaries — something logs and metrics alone cannot show.
- **Example:** a trace for a checkout might show `Client → API Gateway → User Service → Order Service → Payment Service → Notification Service`, with the duration of each hop, making a slow or failing step obvious.

## Correlation IDs

A **correlation ID** (also called a request ID or trace ID) is a unique identifier created when a request first enters the system and then passed along to every service it touches.

Because the same ID appears in the logs, metrics and traces of every service, engineers can tie together *all* the data for a single request. Without correlation IDs, the three pillars are disconnected islands; with them, you can reconstruct the full story of one request end to end.

## The Golden Signals

The **four golden signals** are the key indicators of user-facing health, popularized by Google's SRE practice:

- **Latency** — how long requests take (and, importantly, the latency of *failed* requests separately).
- **Traffic** — how much demand the system is receiving (e.g. requests per second).
- **Errors** — the rate of failing requests.
- **Saturation** — how "full" the system is (e.g. CPU, memory, queue depth).

Watching these four signals gives a compact, high-level picture of whether users are being served well.

## Monitoring vs Observability

- **Monitoring** watches for *known* problems. You decide in advance what to measure and alert on, so monitoring answers "is this specific thing working?" — the known unknowns.
- **Observability** lets you investigate *unknown* problems. It gives you enough rich data to ask new questions after the fact and answer "why is this happening?" — the unknown unknowns.

The two are complementary: monitoring tells you *that* something is wrong; observability helps you find out *why*.

## Key Takeaways

- Observability = understanding internal state from external outputs.
- The three pillars are **logs** (what happened), **metrics** (how much / how often) and **traces** (where time went across services).
- **Correlation IDs** link the three pillars for a single request.
- The **golden signals** (latency, traffic, errors, saturation) summarize user-facing health.
- Monitoring finds known problems; observability explains unknown ones.
