# 36 — Console Backend Audit

## Scope
`JunifyDBServer` HTTP API: routes, auth, validation, error responses, static serving.

## Expected Behavior
Every console panel's call succeeds against real endpoints with correct contracts; invalid input rejected cleanly; auth enforced when configured.

## Current Implementation
Single-file server (2,634 lines) with ~20 inner handlers: health, metrics, stats, engines, collections (CRUD+query), KV (incl. lists/sets/hashes), columns, vectors (128-dim HNSW), schema, transactions, indexes, backup, CDC, audit, SQL. Session manager + API-key filter; security headers; static file serving with CSP.

## Validation Performed
- Contract probing of every endpoint used by the redesigned console (prior sessions: KV/lists/sets/hashes/columns/vectors/schema/transactions/backup/CDC/audit/sql exercised live with real payloads and error paths).
- `ConsoleFeatureValidationTest`, `ConsoleComprehensiveFeatureProofTest`, `BrowserConsoleWorkflowVerificationTest`, `SecurityEnforcementTest`, `PortManagementTest` green in all full runs.
- Auth-on mode exercised by `SecurityEnforcementTest` (401 without key).

## Evidence
- Prior console network traces (`docs/browser-testing/evidence/network/`).
- Server-side handler contracts mapped in audit sessions (sets expect `members`; transaction ids are ints; vectors 128-dim — all surfaced by live testing and fixed in the UI).

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| CB-01 | CONFIRMED | Medium | Handler contracts are undocumented and inconsistent (e.g. `values` vs `members` naming, int transaction ids) — discovered only by probing. API reference needed. |
| CB-02 | CONFIRMED | Medium | Vector endpoint hardcodes 128 dimensions; other dims error — constraint now labeled in UI; configurable dims post-1.0. |
| CB-03 | ACCEPTABLE | Low | Inner handlers excluded from JaCoCo (POM exclusion) — mitigated by the console integration tests; flagged in 44. |

## Improvement Plan
Publish REST API reference; split server into per-handler classes; unify error envelope `{error, status}`.

## Acceptance Criteria
All console tests green (met); UI↔backend contract mismatches fixed (met in UI rewrite).

## Final Status
**CONDITIONAL PASS**
