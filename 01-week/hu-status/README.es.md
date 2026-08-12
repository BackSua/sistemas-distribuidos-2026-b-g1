<!-- PLANTILLA HU-STATUS (traducción al español) - NO borres los marcadores <!-- ... -->
     ni las cabeceras de tabla.
     ATENCIÓN: la nota semanal se lee AUTOMÁTICAMENTE del archivo en inglés:
       01-week/hu-status/README.md  (dentro de TU fork).
     Este archivo es una copia en español para lectura y no se califica. -->

# Estado Semanal - Semana 01

<!-- CONFIG-START - debe coincidir con el CONFIG de tu repo de perfil (username/username) -->
- FULL_NAME: Bairon Alexander Suarez Camacho
- GITHUB_USER: BackSua
- TEAM: The Illusionists
- SPRINT_GOAL: Convertir el brief de OptiView en un mapa de contextos acotados para el dominio Pacientes, un product brief para ms-pacientes, un ADR para el estilo arquitectónico de microservicios y un backlog verificable de historias de registro de pacientes y fórmulas ópticas.
<!-- CONFIG-END -->

## 1. Historias de usuario trabajadas esta semana

| HU ID | Título | Estado (todo/doing/done) | Evidencia (URL de PR o commit) |
|---|---|---|---|
| HU-OPT-001 | Escribir el product brief (prd.md) para el bounded context de Pacientes | done | https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/c83b050 |
| HU-OPT-002 | Definir mapa de contextos y lenguaje ubicuo para ms-pacientes | done | https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/c83b050 |
| HU-OPT-003 | Diseñar el esqueleto de arquitectura hexagonal para ms-pacientes (puertos, adaptadores, entidades de dominio) | doing | Rama hu-opt-003-dev aún no mergeada |
| HU-OPT-004 | Escribir ADR-001: decisión microservicios vs. monolito modular | todo | Pendiente — rama hu-opt-004-dev no creada aún |

## 2. Mi contribución individual

- Escribí el product brief (`prd.md`) para el bounded context de Pacientes de OptiView: contexto inicial, necesidades y problemas, proceso actual, preguntas abiertas y glosario de negocio (Paciente, FórmulaÓptica, DistanciaPupilar, ControlPeriódico, eventos de dominio). Dejé el alcance explícito: `ms-pacientes` posee únicamente los registros clínicos; no persiste órdenes, inventario ni facturación.
- Definí el **mapa de bounded context para ms-pacientes**: identifiqué el Aggregate Root (`Patient`), los Value Objects (`OpticalFormula` con parámetros completos OD/OI, `PupillaryDistance`), las Entities (`PeriodicControl`) y los Domain Events (`PatientRegistered`, `FormulaAdded`, `PeriodicControlExpired`). Cada término tiene una entrada precisa en el lenguaje ubicuo para que todo el equipo comparta un vocabulario sin ambigüedad.
- Apliqué el material de la Sesión 1 de la semana: elaboré primero el **context map** — confirmando que Pacientes, Inventario, Órdenes y Facturación son cuatro bounded contexts reales con reglas de negocio, equipos y necesidades de despliegue distintas — antes de proponer cualquier frontera de servicio.
- Inicié el **ADR-001 (estilo arquitectónico)**: contexto = cuatro dominios distintos, necesidad real de despliegue independiente (equipo Java para Pacientes e Inventario, equipo Go para Órdenes y Facturación), integración orientada a eventos requerida por el curso de Sistemas Distribuidos; decisión = cuatro microservicios con una única instancia PostgreSQL (cuatro schemas aislados) conectados por eventos RabbitMQ. Alternativas rechazadas: monolito modular (el curso y el producto requieren despliegue independiente real) y BD separada por servicio (sobrecarga operativa no justificada para el tamaño actual del equipo).
- Apliqué la **regla de extracción de microservicio** de la sesión (frontera real + necesidad real de escala/despliegue). Ambas condiciones se cumplen: Pacientes tiene sus propios invariantes clínicos, su propio schema, su propio ciclo de despliegue y el equipo está dividido por lenguaje y dominio — así que la decisión queda documentada como cuatro microservicios de bounded context y no como un monolito distribuido.
- Bosquejé el **capeado hexagonal** para `ms-pacientes` (Java 21 / Spring Boot 3): capa `domain` (`Patient`, `OpticalFormula`, `PeriodicControl`) sin imports de I/O; capa `application` (casos de uso `RegisterPatient`, `AddOpticalFormula`, `SearchPatient`, `TriggerPeriodicControlCheck`) que dependen únicamente de interfaces de puerto; capa `infrastructure` (`PatientJpaRepository`, `PatientRestController`, `RabbitMQEventPublisher`) como único lugar que conoce Spring Data JPA o RabbitMQ.
- Derivé el **backlog inicial de ms-pacientes** desde la sección de necesidades, de modo que cada historia se rastrea hasta un problema de negocio declarado: HU-OPT-010 (registrar paciente), HU-OPT-011 (agregar fórmula óptica), HU-OPT-012 (consultar historial de fórmulas con comparativo de evolución visual), HU-OPT-013 (alerta de control periódico), HU-OPT-014 (búsqueda por nombre / documento / código interno).
- Definí las **semánticas de consistencia y entrega** para ms-pacientes según el material de la Sesión 1: registro de paciente = consistencia causal, REST síncrono; alerta de control periódico = eventual, evento asíncrono `PeriodicControlExpired` vía RabbitMQ con entrega at-least-once + llave de idempotencia `(patientId + controlPeriod)` para que no se envíen recordatorios duplicados.

## 3. Bloqueos y riesgos

- Dos preguntas abiertas del brief bloquean los criterios de aceptación de HU-OPT-011 y HU-OPT-012: si una fórmula es inmutable una vez que se crea una orden de trabajo a partir de ella (cambia el invariante del agregado y el modelo de compensación), y si se soportan múltiples optómetras por sede (cambia el modelo de asignación de `PeriodicControl`).
- Las ramas `develop` y `qa` aún no existen en el repositorio grupal, por lo que esta semana no se pudo ejercitar el flujo de rama HU + PR por entorno — solo existe `main`.
- Riesgo de desbordamiento de dominio: ms-ordenes necesitará datos de paciente al crear una orden. El equipo debe garantizar que los lea únicamente a través del puerto REST de `ms-pacientes` — nunca consultando `patients_schema` directamente. Esta frontera necesita un gate explícito en CI.

## 4. Plan para la próxima semana

- Cerrar las dos preguntas abiertas con el equipo y convertir cada respuesta en un criterio de aceptación para HU-OPT-011 y HU-OPT-012.
- Crear las ramas `develop` y `qa`; abrir `hu-opt-003-dev` con un PR hacia `develop` con el esqueleto hexagonal de ms-pacientes.
- Implementar los agregados `Patient` y `OpticalFormula` en Java con pruebas unitarias de todos los invariantes de dominio: rechazo de fórmula vacía, rechazo de esfera negativa, rechazo de DP fuera de rango. La capa de dominio debe alcanzar 100% de cobertura unitaria antes de escribir cualquier código de adaptador.
- Definir las tablas PostgreSQL de `patients_schema` (`patients`, `optical_formulas`, `periodic_controls`) y escribir el script de migración Flyway `V1__create_patients_schema.sql`.
- Publicar ADR-001 como `docs/adr/adr-001-architectural-style.md` (formato Contexto / Decisión / Alternativas / Consecuencias).

## 5. Autoevaluación de cumplimiento

- [x] Conventional Commits - `type(scope): summary`
- [ ] Rama HU + PR por entorno (hu-xxx-dev -> develop, ...)
- [ ] Criterios de aceptación verificables
- [ ] Pruebas agregadas o actualizadas (unitarias / integración)
- [ ] Límites DDD / hexagonal respetados (el dominio no tiene I/O)
- [x] Sin secretos; configuración por variables de entorno

Notas sobre los ítems sin marcar:
- Hasta ahora solo existe `main`; no se pudo abrir ninguna rama HU ni PR hacia `develop`.
- Los criterios de aceptación de HU-OPT-011 y HU-OPT-012 están bloqueados por las preguntas abiertas de la sección 3.
- Esta semana no se escribió código de producción; todo el trabajo es diseño, modelado y documentación.
- El capeado hexagonal está diseñado y revisado, pero aún no materializado en código.

## 6. Enlaces de evidencia

- Product brief: [`prd.md`](./prd.md) — PRJ-OPTIVIEW-PACIENTES (contexto, necesidades, proceso actual, preguntas abiertas, glosario).
- Commit del repositorio: https://github.com/BackSua/sistemas-distribuidos-2026-b-g1/commit/c83b050
- Material del curso (OVAs): https://code-corhuila.github.io/ova-web/2026-B/distribuidos/
- Resumen de la sesión usado para la decisión arquitectónica — fuente vectorial: [`resumen_sistemas_distribuidos_semana_1.svg`](./resumen_sistemas_distribuidos_semana_1.svg)

![Sistemas Distribuidos - Resumen Semana 1: fundamentos distribuidos, ingeniería profesional, ADR y backlog](./resumen_sistemas_distribuidos_semana_1_preview.png)

Principio clave tomado del material: **las fundaciones no son negociables** — DDD, arquitectura hexagonal, SOLID, Clean Code, TDD y CI/CD son el piso, no una opción. "MVP" reduce alcance, nunca estándares.
