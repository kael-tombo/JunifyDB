# 14 — Concurrency & Thread Safety

## Scope
Thread-safety of engines, MVCC, buckets, metrics, event bus under concurrent access.

## Expected Behavior
No lost updates or corruption under concurrent use from multiple threads; reads never block writers (MVCC claim).

## Current Implementation
- Engines: `ConcurrentHashMap` stores (InMemory/File), `ReentrantReadWriteLock` around LSM memtable, synchronized flush/compaction.
- MVCC: lock-free chain reads; ConcurrentHashMap version store.
- WAL: synchronized `log()`/`fsync()`.
- Metrics: atomics (recordInsert etc.); EventBus: synchronized listener list.

## Validation Performed
`ConcurrencyTest`, `LoadAndChargeTest` (demo), and the stress rows in the demo suite run green in baseline + post-fix suites. LSM read path now iterates a copied snapshot of the SSTable list (avoids ConcurrentModificationException if compaction swaps the list mid-iteration — improvement introduced with the ordering fix in 15/16).

## Evidence
Full-suite logs; LSM read-path code diff in this audit.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| C-01 | CONFIRMED (fixed) | Medium | LSM `get()`/`scanValues()` iterated the live `sstables` list while compaction could mutate it on the scheduler thread; now iterate an immutable snapshot. |
| C-02 | CONFIRMED | Medium | FileEngine async flush serializes all writes through one writer thread (throughput bound, not a correctness issue). |
| C-03 | ACCEPTABLE | Low | 50-thread demo run (14,881 ops/sec, 0 violations, per demo harness output) supports basic safety claims; it is not a formal race-detector (JCStress) result — noted as post-release work. |

## Improvement Plan
Add JCStress or long-running randomized concurrency harness as a scheduled CI job post-release.

## Acceptance Criteria
Concurrency suites green (met); no blocking-read regressions (met).

## Final Status
**CONDITIONAL PASS**
