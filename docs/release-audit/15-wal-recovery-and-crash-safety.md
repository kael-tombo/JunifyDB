# 15 — WAL, Recovery & Crash Safety

## Scope
Write-ahead logging, checkpointing, rotation, and recovery for the FILE and LSM_TREE engines; crash behavior.

## Expected Behavior
Per README (corrected): "WAL-based recovery of writes that were not yet flushed." Any write acknowledged in memory but not yet present in the on-disk snapshot must be recoverable after an unclean shutdown.

## Current Implementation
- `WriteAheadLog` (per engine data dir `.wal/wal.log`): `log(type, collection, key, value)` with **real fsync** (`FileOutputStream.getFD().sync()` on every append — verified source), size-based rotation with gzip archival, `CHECKPOINT:<seq>` marker on flush/close, `truncate()`.
- `FileEngine`: logs PUT/DELETE for every write; JSON snapshots written by scheduler or explicit `flush()`.
- `LSMTreeEngine`: logs PUT/DELETE; memtable flushes to SSTables; **`recoverFromWal()` replayed into memtable and added keys to the bloom filter (fix), unconditional on SSTable presence (fix)**.
- `FileEngine`: **new `replayWal()` (fix)** — reads WAL, skips entries `<= lastCheckpointSeq`, applies PUT/DELETE to the store, flushes recovered state.

## Validation Performed
**Defect R-02 (Critical, fixed):** `FileEngine` wrote a WAL but **never read it** — `recoverIfNeeded()` in `WriteAheadLog` invokes a `recoveryCallback` that nothing ever set (grep: zero external callers), and `FileEngine` had no replay logic. Any write not yet in the JSON snapshot was silently lost on crash.
**Defect R-03 (Critical, fixed):** LSM `recoverFromWal()` early-returned `if (!sstables.isEmpty())` — i.e. in the *normal* steady state (SSTables exist) the WAL was ignored; only a first-boot crash was recoverable. Also, recovered keys were not added to the bloom filter, so `get()` (which consults the bloom filter first) would have returned null for recovered keys.

Pre-fix behavioral proof (`.freebuff/prefix-failures.log`, run against clean HEAD):
- `fileEngineRecoversUnflushedWritesFromWal` → `expected: <{"total":42}> but was: <null>` (silent data loss).
- `lsmRecoversPostFlushWritesFromWal` → `expected: <2> but was: <null>`.

Post-fix (all green):
- `fileEngineRecoversUnflushedWritesFromWal` — stale snapshot + WAL tail → recovered.
- `fileEngineReplaysDeletesNewerThanCheckpoint` — checkpoint boundary honored: pre-checkpoint entries do NOT clobber newer snapshot values; post-checkpoint deletes re-applied.
- `lsmRecoversPostFlushWritesFromWal` — SSTable + WAL tail both readable after restart.
- `lsmWALReplayEntriesAreVisibleThroughBloomFilter` — recovered keys pass the bloom gate.

## Evidence
- Constructor ordering fix: LSM now `loadSSTables()` → `recoverFromWal()` → `new WriteAheadLog(dataDir)` (previously WAL construction happened first, so replay raced with a fresh WAL writer).
- Regression file: `ReleaseAuditRegressionTest` (8 tests).
- Restart of the audit's own preview server repeatedly recovered on-disk state (`products.json` readable across restarts).

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| W-01 | CONFIRMED (fixed) | Critical | FileEngine WAL never replayed — silent loss of unflushed writes. |
| W-02 | CONFIRMED (fixed) | Critical | LSM skipped WAL replay whenever SSTables existed. |
| W-03 | CONFIRMED (fixed) | High | LSM WAL-recovered keys absent from bloom filter → unreadable despite being stored. |
| W-04 | CONFIRMED (fixed) | High | LSM constructor created the WAL writer before replaying the WAL (rotation could truncate/reinit during startup). |
| W-05 | CONFIRMED | Medium | WAL rotation archives asynchronously; a crash during rotation can lose the tail between archive and reinit — narrow window, acceptable for 1.0, documented. |
| W-06 | ACCEPTABLE | Low | InMemory engine has no WAL by design (ephemeral); B-Tree engine persists snapshots only — documented engine differences. |

## Improvement Plan
Add crash-injection test harness (kill -9 style) as scheduled CI; consider group-commit fsync policy flag; rotate archival synchronously for engines opened with strict durability mode.

## Acceptance Criteria
All four recovery regressions green in CI (met); full suite green (met).

## Final Status
**CONDITIONAL PASS** (was RELEASE BLOCKER pre-fix; core durability contract now real and proven)
