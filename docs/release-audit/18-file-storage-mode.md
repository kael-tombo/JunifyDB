# 18 — File Storage Mode

## Scope
The FILE engine: layout, flush, durability, restart, corruption behavior.

## Expected Behavior
Human-readable per-collection JSON snapshots + WAL recovery; graceful handling of corrupted or foreign files in the data dir.

## Current Implementation
- One `<collection>.json` per collection (Jackson-serialized map); `.wal/` for the log; `loadAll()` reads every `*.json` in the dir at startup.
- Async flush via scheduler (default 1000ms) or sync (`--sync`); `replayWal()` added in this audit.
- `collectionNames()` (new SPI) reports on-disk collections for restart enumeration.

## Validation Performed
- Recovery regressions (see 15) green.
- Live preview: products collection persisted and re-readable across three server restarts.
- Corruption probe: `loadAll()` throws `RuntimeException("Failed to load: ...")` on malformed JSON → database refuses to open a corrupted dir (fail-fast, defensible); documented here as behavior.

## Evidence
Regression tests; preview server logs; source read of `loadAll`, `syncFlush`, `asyncFlush`.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| F-01 | CONFIRMED (fixed) | Critical | No WAL replay pre-fix (see 15). |
| F-02 | CONFIRMED | Medium | A single corrupted JSON file prevents the whole database from opening (fail-fast but no per-file quarantine). Documented; quarantine = post-release improvement. |
| F-03 | CONFIRMED | Low | Non-JSON files in the data dir are ignored silently (safe default). |

## Improvement Plan
Per-file quarantine + health report on startup; checksum on snapshots.

## Acceptance Criteria
Recovery regressions green (met); fail-fast behavior documented (done here).

## Final Status
**CONDITIONAL PASS**
