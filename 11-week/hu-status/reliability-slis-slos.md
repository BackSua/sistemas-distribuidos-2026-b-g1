# Week 11 — Session 2: SLIs, SLOs, Error Budgets and Alerting

**Topic:** Planning for reliability (Site Reliability Engineering)
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

Observability tells you *what is happening* in a system. The next question is: *how reliable should the system be, and how do we protect that reliability?* This is the job of **SLIs, SLOs, error budgets and alerting** — the core planning tools of Site Reliability Engineering (SRE). Together they turn "the system should be reliable" into concrete, measurable targets and a disciplined way to decide when to ship new features and when to stop and stabilize.

## Why Reliability Needs Targets

100% reliability is both impossible and economically wrong as a goal. Distributed systems always have failures — hardware faults, network issues, dependency outages — and chasing the last fraction of a percent costs enormous effort for benefits users rarely notice. 

So instead of "never fail", teams define **how reliable is reliable enough**: an explicit target that balances reliability against the speed of delivering new features. *Reliability is a feature, but it has a cost — targets let you choose that cost deliberately.*

## SLI — Service Level Indicator

An **SLI** is a quantitative measure of one aspect of the service's health, measured from the user's point of view.

- **Common SLIs:** availability (% of successful requests), latency (how fast requests complete), error rate, throughput.
- **Good SLIs** are measured at the user boundary, are objective, and are hard to game.
- **Example:** "the percentage of requests served in under 300 ms" is a latency SLI.

## SLO — Service Level Objective

An **SLO** is a target value (or range) for an SLI, measured over a time window.

A complete SLO states four things: the **SLI** (what is measured), the **target** (e.g. 99.9%), the **time window** (e.g. rolling 30 days), and the **scope** (which service or endpoint).

- **Example:** "99.9% of requests succeed over a rolling 30-day window."
- SLOs should reflect real user needs and business goals — not simply the highest number achievable.

## SLA — Service Level Agreement

An **SLA** is a formal contract with customers that includes **consequences** (such as service credits or penalties) if the agreed level is not met.

- SLAs are usually set *looser* than internal SLOs, so the team has a safety margin and fixes problems before any contract is breached.
- Not every service has an external SLA; internal services may have only SLOs.

## Error Budget

The **error budget** is the amount of unreliability you are *allowed* to have: `error budget = 100% − SLO`.

- **Example:** a 99.9% SLO leaves a 0.1% error budget — roughly **43 minutes of downtime per month** (over 30 days, 0.1% ≈ 43.2 minutes).
- The budget is something to **spend**, not hoard: it can be used on releases, experiments and planned risk.
- **Policy:** while budget remains, the team can ship quickly; when the budget is exhausted, the team slows down or freezes risky changes and focuses on stability until reliability recovers.

This reframes the usual tension between development (wants to ship fast) and operations (wants stability) into a shared, data-driven rule.

## Alerting on SLOs

Alerts should tell a human when **users are being hurt**, not fire on every internal hiccup.

- **Alert on symptoms, not causes:** page when user-facing SLIs degrade, not on every CPU spike.
- **Burn-rate alerts:** alert on how fast the error budget is being consumed. A fast burn (e.g. 2% of the monthly budget in an hour) is urgent; a slow burn is a ticket, not a 3 a.m. page.
- **Keep alerts actionable:** every alert should require a human decision or action. Too many noisy, non-actionable alerts cause **alert fatigue**, and real problems get ignored.
- Fewer, more meaningful alerts lead to faster response and a healthier on-call rotation.

## The Reliability Loop

These concepts form a continuous cycle:

1. **Measure** — collect the SLI.
2. **Set a target** — define the SLO.
3. **Track** — monitor the error budget.
4. **Alert** — notify on symptoms and burn rate.
5. **Decide** — if budget remains, ship new features; if it is exhausted, stabilize.

Then repeat, adjusting targets as the system and user expectations evolve.

## Key Takeaways

- **SLI** = what you measure; **SLO** = the target for it; **SLA** = the contract with consequences.
- **Error budget** = 100% − SLO = the allowed unreliability; spend it on change, freeze when it runs out.
- **Alert** on user-facing symptoms and burn rate, and keep alerts actionable to avoid fatigue.
- The reliability loop ties it together: measure → target → track → alert → decide.
