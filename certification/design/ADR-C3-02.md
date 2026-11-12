# ADR-C3-02: Tag every image with the release version and deploy the same tag to qa and production

## Status

Proposed

## Context

The workflow `publish.yml` of `opti-products-api` pushes the image with the tag `latest` every time there is a push to `develop`, and the other `opti-*` repositories do the same. Section 5 of the design note shows the consequence: the image that runs in production is whatever `latest` pointed to at the moment of the deploy, so the previous version cannot be named, and a rollback means rebuilding the old commit. It also means qa and production can run different images with the same tag. The release steps of the course (tag, deploy, smoke check, keep or roll back) assume that a rollback is fast and safe, and that cannot be true with `latest`.

## Decision

Every image is built once and tagged with the version of the release (for example `v3.0.0`) and with the short hash of the commit. The pipeline deploys that tag to qa. After the smoke checks pass and a person approves, the same tag, never a new build, is deployed to production. A rollback is a deploy of the previous tag. Database changes follow the same rule: each Flyway migration of the `*-db` repositories must be reversible, or the release notes must say that the rollback needs a restore from backup. The tag `latest` stays only as a convenience for the dev environment.

## Alternatives considered

- Keep `latest` and rebuild the old commit when something fails: rejected because it is slow, depends on the build still working months later, and cannot prove that the image in production is the one that was tested in qa.
- Build a new image for each environment from its branch: rejected because production would run code that nobody tested as an image, and differences between builds (dependencies that moved) could appear only in production.
- Deploy automatically to production on every merge to `main`: rejected for MVP 3 because `10-devops/environments.md` asks for approval in production, and the team has no load or chaos test yet to trust an unattended release.

## Consequences

- Positive: a rollback is a known and fast action, and what runs in production is exactly what passed in qa.
- Positive: the release notes, the tag of the repository and the image share one version, so the incident reports can say which version failed.
- Negative: the pipelines of the `opti-*` repositories must be changed, and each release needs a version decision.
- Negative: reversible migrations are more work to write and to test, and some changes (dropping a column) cannot be reversed without a restore.
