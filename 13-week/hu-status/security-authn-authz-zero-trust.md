# Week 13 — Session 1: Security in Distributed Systems — AuthN, AuthZ and Zero Trust

**Topic:** Security of distributed systems
**Course:** Sistemas Distribuidos (2026-B, G1)

## Overview

A distributed system has many services and many entry points — and an attacker only needs to find one weak door. This session covers how to control who gets in and what they can do: **authentication** and **authorization** with tokens (JWT, OAuth2, OIDC), the **API gateway** as the auth choke point, **service-to-service trust**, the **Zero Trust** mindset, and the **common vulnerability classes** you must design against. The guiding idea: *many doors, one mindset — never trust, always verify.*

## Why Security Is Hard Here

In a monolith there is essentially one door. In a distributed system there are many services, APIs and network paths, each a potential entry point. Security is only as strong as the weakest one, so defense must be consistent everywhere, not just at the front edge.

## Authentication (AuthN) vs Authorization (AuthZ)

These are two different questions, often confused:

- **Authentication (AuthN)** proves **WHO you are** — your identity. *Example: logging in with a password, MFA, or a certificate.* It answers "Who are you?"
- **Authorization (AuthZ)** decides **WHAT you are allowed to do** — your permissions. *Example: role- or attribute-based access control.* It answers "What can you do?"

You authenticate first, then authorize each action.

## Tokens: JWT, OAuth2, OIDC

- **OAuth2** is an authorization framework: it lets an app get **delegated access** to resources with limited **scopes**, without sharing the user's password.
- **OIDC (OpenID Connect)** adds an **identity layer** on top of OAuth2, so the app also learns **who** the user is (via an ID token).
- **JWT (JSON Web Token)** is a **signed, self-contained** token carrying **claims** — who the user is, their scopes, and an expiry. Because it is signed, services can verify it without a database lookup.

Together these let a user log in once and carry a verifiable token across many services.

## The API Gateway as the Auth Choke Point

The **API gateway** is a single entry point in front of the internal services. It is the natural place to enforce security consistently:

- Validate tokens (signature, expiry, issuer).
- Enforce authorization (scopes, roles, policies).
- Apply rate limiting and throttling.
- Filter malicious traffic and provide auditing/observability.

By centralizing these checks, every request is vetted **before** it reaches an internal service.

## Service-to-Service Trust

Internal calls must also be authenticated — a service is **not** trusted just because it is "inside" the network. Common approaches: **mTLS (mutual TLS)** so both sides present certificates, and **short-lived service tokens** scoped to what each service may call. This prevents a single compromised service from freely calling everything else.

## Zero Trust

**Zero Trust** means *never trust, always verify*:

- Authenticate and authorize **every** request, from anywhere.
- Assume the network is **hostile** — there is no safe "inside".
- Grant **least privilege** — each identity gets only the access it needs.
- Verify continuously, using identity, device and context.

It replaces the old "hard shell, soft center" model with verification at every step.

## Common Vulnerability Classes to Design Against

- **Broken authentication** — weak login, poor session/token handling.
- **Broken access control** — users can reach data or actions they shouldn't.
- **Injection (SQL / command)** — untrusted input executed as code or queries.
- **Secrets leakage** — keys, passwords or tokens exposed in code, logs or configs.
- **Insecure dependencies** — vulnerable third-party libraries and base images.

Designing against these from the start is far cheaper than patching them after a breach.

## Key Takeaways

- **AuthN** = who you are; **AuthZ** = what you can do.
- **OAuth2** delegates access, **OIDC** adds identity, **JWT** carries signed claims.
- The **API gateway** centralizes token validation, authZ and rate limiting.
- Authenticate **service-to-service** calls too (mTLS / short-lived tokens).
- **Zero Trust**: verify every request, assume a hostile network, least privilege.

## Infographic

![Security in Distributed Systems: AuthN, AuthZ and Zero Trust](./security-authn-authz-zero-trust-infographic.jpeg)
