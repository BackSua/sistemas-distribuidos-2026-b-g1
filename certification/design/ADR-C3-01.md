# ADR-C3-01: Run the services on a container cluster in two availability zones with managed databases

## Status

Proposed

## Context

OptiView runs today as containers on a single Docker host per environment (`05-architecture/deployment.md`). The host is a single point of failure: if it stops, the sale flow stops, and so do the stock reservations of the saga `place-order`, which is why section 2 of the design note lists reliability first. The databases follow ADR-003 (one database per service), but they are containers on the same host, so a failed host also loses access to all of them at once. Section 4 of the design note sets an RTO of 30 minutes and an RPO of 5 minutes for `opti-sales-db`, and a single host cannot meet those numbers.

## Decision

We run the services (`opti-auth-api`, `opti-customers-api`, `opti-products-api`, `opti-sales-api`, `opti-workflow` and `opti-worker`) as containers in a managed cluster (CCE) with nodes in two availability zones, at least two pods per service, behind a load balancer. Each database (`opti-auth-db`, `opti-customers-db`, `opti-products-db` and `opti-sales-db`) becomes a managed PostgreSQL instance with a primary in the first zone and a standby in the second. The saga state of `opti-workflow` moves to managed Redis and the events of the outbox to managed RabbitMQ. The Dockerfiles of each repository (`deploy/Dockerfile`) do not change; only the place where the containers run changes.

## Alternatives considered

- Keep one Docker host and add a second host with a manual failover: rejected because the failover is a person typing commands, which cannot meet 30 minutes at night, and the databases would still be replicated by hand.
- One shared PostgreSQL instance with four schemas (the idea of `05-architecture/deployment.md` for the first cut): rejected because it contradicts ADR-003 and makes the four contexts fail together.
- Run everything in a single availability zone and rely on backups only: rejected because the RPO of 5 minutes would depend on a restore after every failure of a zone, and the RTO would be the time of that restore.

## Consequences

- Positive: a zone can fail and the sale flow continues, because the load balancer uses the pods of the other zone and the standby database is promoted. Each context can scale and be restored on its own, which is what ADR-003 wanted.
- Positive: the team stops operating databases, backups and failover by hand.
- Negative: the monthly cost is higher than one host, because there are at least two pods per service and a standby per database. The cost principle in section 2 is handled with small nodes and auto scaling, not by removing the second zone.
- Negative: the team has to learn the managed services and the cluster, and the services must keep their connection pools under the limits of each database instance.
