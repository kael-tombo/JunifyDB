# 02 — Product Vision & Positioning

## Scope
Vision documents, README positioning, and their consistency with shipped code.

## Expected Behavior
All public positioning documents agree with each other and with the implementation; unsupported claims removed; contradictions treated as release-quality defects (rule 14).

## Current Implementation
- `README.md` — "H2 for NoSQL" positioning; dual-engine (NoSQL + SQL); 4 engines; console; demos.
- `docs/vision/CORRECTED-PROJECT-VISION.md` — declared SQL "OUT OF SCOPE / does not feature an SQL parser".
- `docs/features/BROKEN-FEATURES.md` — claimed `db.execute("SELECT...")` throws because the parser is "intentionally not implemented".
- Code: `SqlParser`/`SqlEngine` (950+ lines) ship, parse and execute SELECT/INSERT/UPDATE/DELETE with JOIN/GROUP BY, and are covered by `SqlEngineTest` (9 tests) and the console SQL Studio.

## Validation Performed
- Read all three documents (this audit and the prior deep-assessment thread).
- Executed SQL through the live console (`INSERT INTO products ...`, `SELECT *`) — the SQL engine demonstrably works, contradicting the vision doc.

## Evidence
- Live console round-trips recorded in `docs/admin-console/CONSOLE-UI-VALIDATION-PROOF.md` and this audit's preview session.
- `grep -n "ANSI" README.md` before fix: line 6, 73, 129, 185.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| V-01 | CONFIRMED (fixed) | High | README vs vision-doc vs features-doc told three different stories about SQL. Resolution: SQL is real and shipped; vision doc annotated as superseded; "ANSI" downgraded to "built-in SQL engine (implementation-defined dialect)". |
| V-02 | CONFIRMED (fixed) | High | "Production-grade", "tamper-evident", measured-looking perf numbers and "<15 ms startup" overclaimed relative to evidence. Reworded in README (see 40, 43). |
| V-03 | CONFIRMED | Medium | Positioning claim "H2 for NoSQL" is fair for Document/KV/Column, but the SQL engine now also competes with H2's core domain; README comparison table states the dialect boundary so the claim is bounded. |

## Improvement Plan
One canonical positioning statement; delete or archive stale vision docs into `docs/history/` with a redirect note.

## Acceptance Criteria
`grep -ri "out of scope" docs/vision/` yields only superseded-annotated files; README makes no claim contradicted by any doc in this audit.

## Final Status
**CONDITIONAL PASS** (contradictions resolved in the working tree; archive/redirect cleanup is post-release).
