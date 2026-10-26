# Week 13 — Session 2: Threat Modeling and the Security Backlog

**Topic:** Planning security into the system
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

Knowing security mechanisms (Session 1) is not enough; you must deliberately **look for the threats** to your own system and plan to fix them. This session covers **threat modeling** — a structured way to find and rate threats during design — and the **security backlog**, where chosen mitigations become real, trackable work. The guiding idea: *find the threats on paper before attackers find them in production.*

## What Is Threat Modeling

Threat modeling is a **structured process to find, rate and address security threats early** — during design, before code is written. Fixing a design flaw on paper is far cheaper and safer than fixing it after it has shipped.

## The Four Key Questions

A good threat model answers four simple questions:

1. **What are we building?** — understand the system, its data and goals.
2. **What can go wrong?** — identify and analyze potential threats.
3. **What are we going to do about it?** — choose mitigations.
4. **Did we do a good job?** — validate that risks were reduced to an acceptable level.

## Map the System

Draw a **data-flow diagram (DFD)** showing:

- **External entities** (users, third-party systems),
- **Processes** (your services),
- **Data stores** (databases, queues), and
- **Trust boundaries** — the lines where data crosses from a less-trusted zone to a more-trusted one.

Trust boundaries are where most threats appear, because that is where untrusted input enters a more privileged area.

## STRIDE Threat Categories

**STRIDE** is a checklist for the kinds of threats to look for:

- **Spoofing** — pretending to be someone else. *e.g. an attacker uses stolen credentials to access an API.*
- **Tampering** — unauthorized modification of data. *e.g. altering a message in transit between services.*
- **Repudiation** — denying an action occurred. *e.g. a user denies a sensitive change and there are no audit logs.*
- **Information disclosure** — exposing sensitive data. *e.g. a misconfigured store leaks records.*
- **Denial of service** — making the system unavailable. *e.g. flooding the API to exhaust resources.*
- **Elevation of privilege** — gaining higher permissions than intended. *e.g. a normal user gains admin access.*

## Rate and Prioritize

Not every threat is equally urgent. Estimate **risk = likelihood × impact**, using a method such as **DREAD** (Damage, Reproducibility, Exploitability, Affected users, Discoverability) or a simple **High / Medium / Low** matrix. This decides what to fix first instead of trying to fix everything at once.

## Mitigations

For each serious threat, choose a response:

- **Mitigate** — add a control that reduces likelihood or impact (the most common choice).
- **Eliminate** — remove the risky feature entirely.
- **Transfer** — shift the risk to a third party (e.g. a managed payment provider, insurance).
- **Accept** — knowingly accept a low risk, within appetite, and monitor it.

## The Security Backlog

Turn the chosen mitigations into **prioritized, trackable work items** — user stories or tickets — that enter the sprint **alongside feature work**, each with:

- a clear **priority** (from the risk rating),
- an **owner**, and
- **acceptance criteria** that define when the mitigation is truly done.

This keeps security from being an afterthought: it competes for time like any other work and is verifiable.

## Key Takeaways

- Threat modeling finds and rates threats **during design**, answering four key questions.
- Map the system with a **data-flow diagram** and mark **trust boundaries**.
- Use **STRIDE** to enumerate threat types and **risk = likelihood × impact** to prioritize.
- Respond with **mitigate / eliminate / transfer / accept**.
- Track chosen mitigations in a **security backlog** with priority, owner and acceptance criteria.

## Infographic

![Threat Modeling and the Security Backlog](./threat-modeling-security-backlog-infographic.jpeg)
