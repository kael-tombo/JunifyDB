# 44 — Build & CI Audit

## Scope
Maven build health, CI workflows, coverage gating, wrappers.

## Expected Behavior
CI green and meaningful; coverage gate enforced; reproducible from clean clone.

## Current Implementation
- Root build: single module; `mvn clean test` green (669 baseline / 677 post-fix). CI (`ci.yml`): Java 21/23 matrix, `mvn -B clean verify`, Codecov upload, test artifacts.
- Coverage: JaCoCo report always; **70% line gate only in `-Pcoverage-check`** (CI does not pass the profile → gate never enforced). 9 packages excluded (console inner handlers, pool, migration, HNSW, compactor, FileEnginePool, Kafka CDC, example, benchmark).
- Docker CI job references **`Dockerfile` which does not exist** → that job fails on every main push.
- `mvnw.cmd` present; **no `mvnw`** shell script for Linux/macOS.
- Starters/demos/cli are separate builds not covered by CI.

## Validation Performed
Two full clean builds in this audit; CI file read; POM profile inspection.

## Evidence
`.freebuff/baseline-build.log`, `.freebuff/postfix-full-suite2.log`; `ci.yml` source.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| CI-01 | CONFIRMED | Medium | Coverage gate decorative (profile not invoked in CI). Fix: add `-Pcoverage-check` to CI after verifying current coverage passes, else lower gate to measured value with a raise-plan. |
| CI-02 | CONFIRMED | Medium | Docker job fails (missing Dockerfile): remove job or add Dockerfile. |
| CI-03 | CONFIRMED | Low | No Linux/macOS wrapper script. |
| CI-04 | CONFIRMED | Medium | Starters/demos untested by CI — the biggest verification gap for "framework support" claims (see 29–32). |
| CI-05 | ACCEPTABLE | Low | Windows path-lock: clean fails if a DB is open on `target/` (OS behavior; documented in run doc). |

## Improvement Plan
CI additions: `-Pcoverage-check`; matrix job for starter modules; demo smoke job; remove/fix Docker job; add `mvnw`.

## Acceptance Criteria
Core CI green (met); gates/gaps documented (met); full CI closure scheduled (60-checklist).

## Final Status
**CONDITIONAL PASS**
