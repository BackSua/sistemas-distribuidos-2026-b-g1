# Product Brief

project_key: PRJ-OPTIVIEW-PACIENTES

## Declared Tech Stack

- backend: Java 21 + Spring Boot 3.x
- database: PostgreSQL 16 (schema: patients_schema)
- messaging: RabbitMQ (domain events producer)
- auth: Keycloak + JWT
- frontend: Angular 18+ (admin portal)

---

## 00 — Initial Context

OptiView is an integral management platform for optical stores and visual-health centres in Colombia and Latin America. This brief covers the **Patients bounded context** — one of the four core domains of the system — implemented as the `ms-pacientes` microservice in Java 21 with Spring Boot 3.

The vast majority of optical stores operate with paper prescriptions: optical formulas are handwritten on cards that get lost, deteriorate and offer no traceability of the patient's visual history. There is no unified digital record, no automated reminder for annual visual check-ups, and no way for the optometrist to compare how a patient's vision has changed over time.

The `ms-pacientes` service owns the complete clinical record of every patient: personal data, optical formula history, periodic-control schedule and search capabilities. It communicates with the rest of the system exclusively through domain events (`PatientRegistered`, `FormulaAdded`, `PeriodicControlExpired`) published to RabbitMQ, and through synchronous REST calls when order creation requires patient data validation.

---

## 01 — Needs and Problems

- Register and manage patient personal data (name, ID, contact, health-insurance / EPS) without paper.
- Record complete optical formulas for each eye: sphere (SPH), cylinder (CYL), axis, addition (ADD), pupillary distance (PD) and recommended lens type.
- Keep a full formula history per patient with a visual-evolution comparison (e.g., myopia progression over 3 years).
- Automatically trigger a periodic-control reminder when the last revision is older than 12 months.
- Search patients instantly by full name, ID number or internal code — replacing manual ledger searches.
- Never allow a formula registered in error to silently produce a wrong order; invariants must be enforced at the aggregate level before any persistence occurs.

Main problem: the optometrist has no immediate access to a patient's clinical history and must reconstruct it manually from paper cards, phone calls or memory — causing errors, delays and patient abandonment.

---

## 02 — Current Process

1. Patient arrives → optometrist writes prescription on a paper card → card is handed to the patient and a copy is filed (or lost).
2. If the patient returns months later, staff searches physical folders by name — average search time: 3–5 minutes per patient.
3. No alert exists for patients who have not had a check-up in over a year; follow-up depends entirely on the patient's own memory.
4. Formula data is never compared across visits; the optometrist cannot objectively track visual-acuity evolution.
5. When an order of work is created, staff must read the paper formula and type it manually into the order — a frequent source of transcription errors.

Expected process with `ms-pacientes`:
1. Patient registered once → digital record persists forever.
2. Full formula entered by the optometrist → validated by domain invariants → stored in `patients_schema`.
3. Order creation queries `ms-pacientes` REST API → formula data pre-filled automatically, no manual retyping.
4. Scheduled job evaluates `periodic_controls` table → emits `PeriodicControlExpired` event → notification service sends reminder.
5. Optometrist opens patient history → sees all formulas chronologically with delta comparison.

---

## 03 — Open Questions

- Should an optical formula be immutable once a work order has been created from it, or can the optometrist issue a correction (requires a compensation / reversal model)?
- Will the system support multiple optometrists per location, and if so, is the periodic-control reminder sent to the patient, the assigned optometrist, or both?
- What is the initial catalogue of lens types to include in the formula form?
- Should the patient record include a photo or scanned ID?
- Is there a role-based access model where a seller can search patients but cannot edit formulas (only the optometrist can)?
- Should `ms-pacientes` also own appointment scheduling, or is that a separate bounded context?

---

## 04 — Business Glossary

- **Patient**: a person registered in the system with a unique internal ID, personal data and one or more optical formulas.
- **Optical formula**: the optometrist's prescription specifying the visual correction required for each eye (OD = right eye, OI = left eye).
- **Sphere (SPH)**: lens power in dioptres. Negative = myopia; positive = hyperopia.
- **Cylinder (CYL)**: astigmatism correction in dioptres.
- **Axis**: orientation angle of the cylinder in degrees (1°–180°).
- **Addition (ADD)**: extra near-vision power for presbyopia patients.
- **Pupillary distance (PD)**: distance between pupils in millimetres; critical for lens centring.
- **Periodic control**: scheduled visual check-up (annual or semi-annual) that the system tracks and reminds the patient about.
- **PatientRegistered**: domain event emitted when a new patient record is persisted successfully.
- **FormulaAdded**: domain event emitted when a new optical formula is added to a patient's history.
- **PeriodicControlExpired**: domain event emitted when a patient's last revision exceeds the configured control interval.
