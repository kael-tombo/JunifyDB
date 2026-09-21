# 35 — Demo Project Audit

## Scope
The 8+ demo projects under `demo/` — buildability, documented-command accuracy, hidden knowledge.

## Expected Behavior
Per the release standard: a new developer can follow documented commands and reproduce demo output without hidden knowledge.

## Current Implementation
`demo/` contains `demo-common` + standalone Maven projects (spring-boot-demo, quarkus-demo, micronaut-demo, vertx-demo, annotation-showcase, advanced-queries, batch-processing, load-and-stress, end-to-end-validation). Each has its own POM, README sections, and tests; they reference `junify-db-core` **1.0.0** — resolved from the local repository after `mvn install` of the core (an implicit prerequisite that must be documented).

## Validation Performed
- Demo sources read; prior sessions executed several demos successfully (annotation-showcase, batch, stress, e2e, spring-boot).
- **Full demo suite re-executed against the fixed build (2026-09-21, post-release-audit fixes):** core installed (`mvn install -DskipTests`, EXIT=0), `demo-common` installed, then `mvn clean test` per demo following `demo/RUNBOOK.md` §1. Results (from each demo's surefire XML, all written during this run):

| Demo | Tests | Failures | Errors | Skipped | Notes |
|---|---|---|---|---|---|
| spring-boot-demo | 6 | 0 | 0 | 0 | Real Spring Boot 3.2.5 context boot; console bound to 9090 |
| quarkus-demo | 4 | 0 | 0 | 0 | Quarkus 3.8.0 started in 4.38s, REST endpoints exercised |
| micronaut-demo | 4 | 0 | 0 | 0 | Micronaut DI + controller tests green |
| vertx-demo | 4 | 0 | 0 | 0 | Verticle deploy + flows green |
| end-to-end-validation | 4 | 0 | 0 | 0 | Multi-engine CRUD/transaction e2e |
| advanced-queries-demo | 5 | 0 | 0 | 0 | SQL/aggregation queries |
| annotation-showcase-demo | 5 | 0 | 0 | 0 | JNoSQL+JPA annotation interop |
| batch-processing-demo | 5 | 0 | 0 | 0 | Atomic batch ingestion + rollback |
| load-and-stress-demo | 6 | 0 | 0 | 0 | Live harness output: 50-thread run 18,382 ops/sec, 0 failures; all 5 load scenarios 0 fail |

**Total: 43 demo tests, 0 failures, 0 errors, 0 skipped across all 9 demos.** Raw logs under `.freebuff/demo-results/` (untracked evidence); stress-harness line output reproduced in the run log.

## Evidence
- Surefire XMLs in each `demo/<name>/target/surefire-reports/` (timestamps match run window 09:46–09:48).
- `.freebuff/demo-results/<demo>.log` — per-demo Maven output.
- Quarkus startup log line: "quarkus-ecommerce-demo 1.0.0 on JVM (powered by Quarkus 3.8.0) started in 4.380s".
- Stress harness: `[LOAD-02] threads=50 ops=2,500 success=2,500 fail=0 throughput=18382.4 ops/sec`.

Prior-session evidence: demo tree, older run records, `MultiEngineE2EValidationTest` references.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| DM-01 | CONFIRMED | Medium | Implicit prerequisite: demos require `mvn install` of the core first (or version bump juggling). Must be step 0 in `demo/README`. |
| DM-02 | CONFIRMED (closed) | ~~Medium~~ → resolved | **All 9 demos re-executed on the fixed build, 43/43 tests green.** The 60-release-checklist pre-tag item is closed. |
| DM-03 | ACCEPTABLE | Low | Demo POMs pin framework versions (Boot 3.2, Quarkus 3.8, Micronaut 4.2, Vert.x 4.5) — matching README badges; confirmed live during this run. |

## Improvement Plan
`demo/README.md` with exact per-demo commands + expected output; root script `scripts/run-all-demos` for CI.

## Acceptance Criteria
Per-demo commands documented (follow-up); no hidden env vars found in demo sources (checked — none).

## Final Status
**CONDITIONAL PASS**
