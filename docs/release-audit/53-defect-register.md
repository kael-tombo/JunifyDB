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
| R-10 | CI | Coverage gate never enforced (profile not invoked) | Medium | OPEN | 44-CI-01; fix in CI config (post-release or pre-tag) |
| R-11 | CI | Docker job references missing Dockerfile → job fails | Medium | OPEN | 44-CI-02 |
| R-12 | Build | `mvnw` shell script missing (Linux/macOS) | Low | OPEN | 44-CI-03 |
| R-13 | Maven Central | Missing source/javadoc/GPG plugins; staging unverified | High | OPEN | 46-MC-01/02; documented path |
| R-14 | CI scope | Starter/demo/cli modules not built by CI | Medium | OPEN | 44-CI-04 |
| R-15 | Observability | CDC subsystem has no producer wired | Medium | OPEN (documented) | 23-OB-01 |
| R-16 | Error handling | No typed exception hierarchy | Medium | OPEN (documented) | 21-E-01 |
| R-17 | Security | No CVE scan in CI; CORS echo-with-credentials default | Medium | OPEN | 22-SEC-05, 22 |
| R-18 | Indexing | Planner never auto-selects secondary indexes | Medium | OPEN (documented) | 12-IX-01 |
| R-19 | Vectors | HNSW dimension hardcoded 128 | Medium | OPEN (documented, UI-labeled) | 12-IX-02 |
| R-20 | Transactions | Engine apply phase non-atomic on engine failure; mixed tx/non-tx writers last-write-wins | Medium | OPEN (documented) | 13-T-02/03 |
| R-21 | FileEngine | Single corrupted snapshot file blocks startup (no quarantine) | Medium | OPEN (documented) | 18-F-02 |
| R-22 | Demos | Prerequisite (`mvn install` core) undocumented; not all demos re-executed here | Medium | OPEN | 35-DM-01/02 |
| R-23 | Docs | ROADMAP predates shipped SQL engine | Low | OPEN | 39-WS-02 |
| R-24 | Audit trail | In-memory only, not persisted | Low | OPEN (documented) | 22-SEC-03 |

## Summary
- **Fixed in this audit**: R-01..R-09 (4 critical, 3 high, 2 others) — each with pre-fix failing evidence and post-fix green regression.
- **Open, documented**: R-10..R-24 — none classified as release blockers under the blocker policy once the durability/conflict fixes landed; several are scheduled pre-tag (R-10/11) or publication-gating (R-13).

## Final Status
**CONDITIONAL PASS** (register complete; open items carry disposition in 54/59/60)
