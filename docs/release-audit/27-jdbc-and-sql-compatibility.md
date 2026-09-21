# 27 — JDBC & SQL Compatibility

## Scope
JDBC driver existence/claims, and SQL compatibility boundary.

## Expected Behavior
No JDBC claims unless a driver exists.

## Current Implementation
**No JDBC driver ships.** `db.sql(...)` is the only SQL entry point; no `java.sql` types are exposed by `SqlResultSet` (rows/columns API only).

## Validation Performed
Repository-wide search for `java.sql`/`Driver`/`Connection` implementations — none in main sources.

## Evidence
Code search across `src/main/java`.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| JC-01 | CONFIRMED | Medium | No JDBC support — must never be implied. README comparison table states the SQL dialect boundary; no JDBC claim exists. |
| JC-02 | ACCEPTABLE | Low | A minimal read-only JDBC driver would broaden adoption but is a 1.2+ project (ROADMAP already scopes SQL evolution there). |

## Improvement Plan
Evaluate a thin JDBC wrapper over `SqlEngine` post-1.0 with an explicit unsupported-feature matrix.

## Acceptance Criteria
No JDBC claim in any public doc (met).

## Final Status
**PASS** (honest absence)
