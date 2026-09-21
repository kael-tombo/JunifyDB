# 23 — Observability & Diagnostics

## Scope
Metrics, health, events, CDC, audit trail, engine stats, JVM diagnostics.

## Expected Behavior
Claims limited to reality: metrics real, CDC real-but-unwired (documented), audit trail honest.

## Current Implementation
- `DatabaseMetrics` (atomic counters, collection sizes); `/api/metrics`, `/api/health`, `/api/engines` endpoints verified live.
- `EventBus` (synchronous listener dispatch, exceptions isolated).
- `CDCManager` + `FileCDCConnector` (+ optional Kafka connector): **no production write path records CDC events** (grep: no `recordInsert/recordUpdate/recordDelete` callers outside the manager itself). The console CDC panel shows connector status — which is real — but no event stream exists. UI wording already neutral ("CDC connector status").
- Audit trail: in-memory `ArrayDeque` ring (not tamper-evident) — README corrected.

## Validation Performed
Code grep across `src/main` for CDC wiring; live metrics/health checks; suite green.

## Evidence
Grep results recorded in audit session; console traces.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| OB-01 | CONFIRMED | Medium | CDC is infrastructure without a producer; harmless but potentially misleading if advertised as a change stream. Not claimed in README; keep it that way. |
| OB-02 | CONFIRMED (fixed, prior session) | Low | Console previously showed fabricated static numbers pre-fetch; redesigned UI renders only server data (console.css/console.js rewrite). |
| OB-03 | ACCEPTABLE | Low | Micrometer integration exists only in the `benchmark` profile — not a supported metrics export path; not claimed. |

## Improvement Plan
Wire CDC recording into document write path behind a flag post-1.0, or remove the CDC panel/claim in favor of the audit trail.

## Acceptance Criteria
No README claim of CDC change streams (met); metrics/health endpoints verified (met).

## Final Status
**CONDITIONAL PASS**
