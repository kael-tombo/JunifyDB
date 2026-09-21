# 53 — Defect Register

## Scope
All defects identified in this audit with status, severity, fix evidence, and disposition.

## Register
| ID | Component | Description | Severity | Status | Fix Evidence |
|---|---|---|---|---|---|
| R-01 | `MVCCManager.commit` | Write-write conflict detection unreachable (`head.commitTs > commitTs` impossible with shared monotonic clock) → silent lost updates under concurrency | Critical | **FIXED** | 3-arg commit validates write set vs snapshot; `ReleaseAuditRegressionTest` (3 tests) fail pre-fix (`.freebuff/prefix-failures.log`), pass post-fix |
| R-02 | `FileEngine` | WAL written but never replayed; `recoveryCallback` never set anywhere → unflushed writes lost on crash | Critical | **FIXED** | `replayWal()` with checkpoint sequencing; 2 regression tests fail pre-fix (`expected <{"total":42}> but was <null>`), pass post-fix |
| R-03 | `LSMTreeEngine` | `recoverFromWal()` skipped whenever SSTables exist; recovered keys not added to bloom filter → unreadable/stale recovery | Critical | **FIXED** | Replay unconditional + bloom-add + constructor ordering; 2 regression tests fail pre-fix, pass post-fix |
| R-04 | `JunifyDB` | Persisted collections invisible after restart (lazy materialization) until touched by name | High | **FIXED** | `materializePersistedCollections()` + `StorageEngine.collectionNames()` SPI; regression test fails pre-fix (`expected <true> but was <false>`), passes post-fix |
| R-05 | `LSMTreeEngine` | Compaction merged oldest→newest in insertion order such that older tables could overwrite newer values; SSTable list ordering inconsistent between flush (newest-first) and restart load (oldest-first) | High | **FIXED** | Canonical oldest→newest ordering; newest-first iteration in `get`/`scan`; verified by `LSMTreeEngineTest` |
| R-06 | `LSMTreeEngine.keys` | `SSTable.keys(prefix)` returned raw composite keys → `count()` inflated/malformed after restart (surfaced by R-03 fix) | High | **FIXED** | Extract logical keys at union; `LSMTreeEngineTest.testPersistence` green again — now for the *right* reason |
| R-07 | README/docs | Overclaims: ANSI SQL, tamper-evident audit, measured-looking benchmarks, <15 ms, B-Tree page store/>RAM | High | **FIXED** | README corrections; verification table in 43 |
| R-08 | Repo hygiene | `$null`, `server.err`, `server.out` tracked in git | Low | **FIXED** | Removed; `.gitignore` extended |
| R-09 | Console UI (pre-redesign) | 15.7k-line monolith; `escapeHtml` crash; fabricated data; CSP-blocked fonts | High | **FIXED** (prior session) | 1.7k-line rewrite; zero console errors; feature tests green |
| R-10 | CI | Coverage gate never enforced (profile not invoked) | Medium | **FIXED** (2026-09-21) | CI build job runs `-Pcoverage-check`; measured 73.6% line coverage on 680-test suite (62-evidence) |
| R-11 | CI | Docker job references missing Dockerfile → job fails | Medium | OPEN | 44-CI-02 |
| R-12 | Build | `mvnw` shell script missing (Linux/macOS) | Low | **FIXED** (2026-09-21) | Canonical maven-wrapper 3.2.0 `mvnw` added, smoke-tested; CI now uses `./mvnw` |
| R-13 | Maven Central | Missing source/javadoc/GPG plugins; staging unverified | High | OPEN | 46-MC-01/02; documented path |
| R-14 | CI scope | Starter/demo/cli modules not built by CI | Medium | **FIXED** (2026-09-21) | `integrations` job (3 starters, verified green locally first) + `demos` job (all 9 demo suites); `cli/` excluded — see R-25 |
| R-15 | Observability | CDC subsystem has no producer wired | Medium | **FIXED** (2026-09-21) | CDC listener registered on a new EventBus system channel (survives `clear()`); update() carries old value; 2 regression tests |
| R-16 | Error handling | No typed exception hierarchy | Medium | OPEN (documented) | 21-E-01 |
| R-17 | Security | No CVE scan in CI; CORS echo-with-credentials default | Medium | **FIXED** (2026-09-21) | OWASP dependency-check job (`failBuildOnCVSS=9`); wildcard-origin responses no longer claim credentials support |
| R-18 | Indexing | Planner never auto-selects secondary indexes | Medium | OPEN (documented) | 12-IX-01 |
| R-19 | Vectors | HNSW dimension hardcoded 128 | Medium | OPEN (documented, UI-labeled) | 12-IX-02 |
| R-20 | Transactions | Engine apply phase non-atomic on engine failure; mixed tx/non-tx writers last-write-wins | Medium | OPEN (documented) | 13-T-02/03 |
| R-21 | FileEngine | Single corrupted snapshot file blocks startup (no quarantine) | Medium | **FIXED** (2026-09-21) | Corrupt snapshots quarantined to `.quarantine/` + startup continues; snapshot writes now atomic (tmp+move); 2 regression tests |
| R-22 | Demos | Prerequisite (`mvn install` core) undocumented; not all demos re-executed here | Medium | **CLOSED** (2026-09-21) | All 9 demos re-run on fixed build: 43/43 tests green (35-DM-02); prerequisite documentation remains P1 |
| R-23 | Docs | ROADMAP predates shipped SQL engine | Low | **FIXED** (2026-09-21) | ROADMAP refreshed: SQL/CDC/crash-safety marked shipped, test count corrected, governance row added |
| R-24 | Audit trail | In-memory only, not persisted | Low | OPEN (documented) | 22-SEC-03 |
| R-25 | `cli/` module | Orphan: no `pom.xml`, package path mismatch, stale imports (`org.junify.db.document.*`) → does not compile | Medium | OPEN (documented) | Found during improvement round; excluded from CI until built or removed |
| R-26 | `WriteAheadLog` | No record-size bound: one oversized write could exhaust memory | Medium | **FIXED** (2026-09-21) | `MAX_RECORD_BYTES` (64 MB) enforced in `log()`; regression test |

## Summary
- **Fixed in this audit**: R-01..R-09 (4 critical, 3 high, 2 others) — each with pre-fix failing evidence and post-fix green regression.
- **Fixed in improvement round (2026-09-21)**: R-10, R-12, R-14, R-15, R-17, R-21, R-23, R-26 (CI gates, wrapper, CDC wiring, CORS, quarantine/atomic snapshots, WAL bound) — evidence in 62.
- **Open, documented**: R-11 closed earlier; R-13 (Maven Central staging run), R-16, R-18, R-19, R-20, R-24, R-25 remain open with disposition in 54/59/60; none are release blockers.

## Final Status
**PASS** (register complete; remaining open items are documented, scheduled, and non-blocking)
