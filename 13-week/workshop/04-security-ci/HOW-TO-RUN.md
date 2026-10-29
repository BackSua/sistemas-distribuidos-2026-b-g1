# How to run the negative authorization test

Story covered: **only `ADMIN` or `SERVICE` may reserve or release stock in `opti-products-api`**
(threats S and E of `stride-BackSua.md`). Acceptance criterion: a token with role `SELLER` calling
`POST /api/v1/frames/{id}/reservations` is refused with `FORBIDDEN` (403), and the token of
`opti-workflow` (`SERVICE`) is accepted.

The test calls the **real** rule of the service, `RequestRules.requireRole(http, Roles.ADMIN,
Roles.SERVICE)` (the same line that guards `FrameController`), compiling the real sources of
`opti-products-api` from a sibling checkout. It needs no database and no running service.

## Requirements

- JDK 21 or newer.
- A checkout of `opti-products-api` (the path below is mine; change `SRC`).
- JUnit 5.11.4 and the Spring 6.1.11 and Jakarta Servlet 6.1.0 jars from the local Maven repository.

## Run (Git Bash on Windows)

```bash
cd 13-week/workshop/04-security-ci/products-authz-test
M=$(cygpath -m ~/.m2/repository)
SRC=$(cygpath -m /c/Distribuidos/repos-desarrollo/opti-products-api)
CP="$M/org/junit/platform/junit-platform-launcher/1.11.4/junit-platform-launcher-1.11.4.jar;$M/org/junit/platform/junit-platform-engine/1.11.4/junit-platform-engine-1.11.4.jar;$M/org/junit/platform/junit-platform-commons/1.11.4/junit-platform-commons-1.11.4.jar;$M/org/junit/jupiter/junit-jupiter-engine/5.11.4/junit-jupiter-engine-5.11.4.jar;$M/org/junit/jupiter/junit-jupiter-api/5.11.4/junit-jupiter-api-5.11.4.jar;$M/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar;$M/org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar;$M/jakarta/servlet/jakarta.servlet-api/6.1.0/jakarta.servlet-api-6.1.0.jar;$M/org/springframework/spring-web/6.1.11/spring-web-6.1.11.jar;$M/org/springframework/spring-core/6.1.11/spring-core-6.1.11.jar;$M/org/springframework/spring-jcl/6.1.11/spring-jcl-6.1.11.jar;$M/com/fasterxml/jackson/core/jackson-annotations/2.21/jackson-annotations-2.21.jar"
SP="$SRC/products-adapters/src/main/java;$SRC/products-core/src/main/java"
OUT=$(cygpath -m /tmp/w13out); mkdir -p /tmp/w13out
javac --release 21 -cp "$CP" -sourcepath "$SP" -d "$OUT" src/test/java/co/edu/corhuila/opti/products/authz/*.java
java -cp "$OUT;$CP" co.edu.corhuila.opti.products.authz.RunTests
```

In the CI of `opti-products-api` the same class runs with `mvn -B test -Dtest='*AuthzTest'` (see
`../security.yml`, job `authorization-tests`).

## Real output on my machine

```text

Test run finished after 202 ms
[         2 containers found      ]
[         0 containers skipped    ]
[         2 containers started    ]
[         0 containers aborted    ]
[         2 containers successful ]
[         0 containers failed     ]
[         2 tests found           ]
[         0 tests skipped         ]
[         2 tests started         ]
[         0 tests aborted         ]
[         2 tests successful      ]
[         0 tests failed          ]

```
