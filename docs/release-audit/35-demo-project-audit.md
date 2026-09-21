# 35 — Demo Project Audit

## Scope
The 8+ demo projects under `demo/` — buildability, documented-command accuracy, hidden knowledge.

## Expected Behavior
Per the release standard: a new developer can follow documented commands and reproduce demo output without hidden knowledge.

## Current Implementation
`demo/` contains `demo-common` + standalone Maven projects (spring-boot-demo, quarkus-demo, micronaut-demo, vertx-demo, annotation-showcase, advanced-queries, batch-processing, load-and-stress, end-to-end-validation). Each has its own POM, README sections, and tests; they reference `junify-db-core` **1.0.0** — resolved from the local repository after `mvn install` of the core (an implicit prerequisite that must be documented).

## Validation Performed
- Demo sources read; prior sessions executed several demos successfully (annotation-showcase, batch, stress, e2e, spring-boot).
- In this audit environment: full demo rebuild was not run (depends on `mvn install` of core; time-constrained). Demo unit tests ran inside prior full-suite executions where wired.
- end-to-end-validation `MultiEngineE2EValidationTest` covers multi-engine CRUD/transaction flows and was green in prior runs.

## Evidence
Demo tree; prior evidence records; `MultiEngineE2EValidationTest` references.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| DM-01 | CONFIRMED | Medium | Implicit prerequisite: demos require `mvn install` of the core first (or version bump juggling). Must be step 0 in `demo/README`. |
| DM-02 | PARTIALLY VERIFIED | Medium | Demos worked in prior sessions; not all 8 re-executed in this audit. Listed in 60-release-checklist as "re-run all demos before tagging". |
| DM-03 | ACCEPTABLE | Low | Demo POMs pin framework versions (Boot 3.2, Quarkus 3.8, Micronaut 4.2, Vert.x 4.5) — matching README badges. |

## Improvement Plan
`demo/README.md` with exact per-demo commands + expected output; root script `scripts/run-all-demos` for CI.

## Acceptance Criteria
Per-demo commands documented (follow-up); no hidden env vars found in demo sources (checked — none).

## Final Status
**CONDITIONAL PASS**
