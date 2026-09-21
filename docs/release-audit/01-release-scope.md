# 01 — Release Scope

## Scope
What is in scope for the 1.0.0 public release, and what is explicitly out of scope.

## In Scope (verified present in repository)
| Area | Evidence |
|---|---|
| Core engine `junify-db-core` v1.0.0 (single Maven module) | root `pom.xml` |
| Storage engines: IN_MEMORY, FILE, B_TREE, LSM_TREE | `src/main/java/org/junify/db/storage/spi/` |
| Multi-model API: Document, KV/List/Set/Hash buckets, ColumnFamily | `src/main/java/org/junify/db/nosql/`, `column/` |
| Built-in SQL engine (SELECT/INSERT/UPDATE/DELETE, JOIN, GROUP BY) | `src/main/java/org/junify/db/sql/` (950+ lines, exercised by `SqlEngineTest`, console SQL Studio) |
| MVCC transactions (snapshot isolation, first-writer-wins) | `src/main/java/org/junify/db/transaction/mvcc/` |
| WAL + crash recovery (File/LSM engines) | `WriteAheadLog.java`, fixed per audit (see 15) |
| Embedded HTTP console (UI + REST API + API-key auth) | `console/http/JunifyDBServer.java`, `static/` |
| Framework integration projects (separate builds) | `spring-boot-starter/`, `quarkus-extension/`, `micronaut-integration/`, `demo/` (vertx) |
| Demos (8+ standalone Maven projects) | `demo/` |
| CLI shell | `cli/` |

## Out of Scope (explicitly not claimed)
- Full ANSI:92 SQL grammar (no DDL, views, sequences, stored procedures, full type system).
- JDBC driver. `27-jdbc-and-sql-compatibility.md` documents the absence.
- SQL-file B-Tree page store; `BTreeEngine` is heap-resident with snapshot persistence only.
- Cryptographically tamper-evident audit log; the audit trail is an in-memory ring buffer.
- Cluster/distributed operation; single-process embedded only.
- Kafka CDC connector as a supported integration (code exists; it is optional, excluded from coverage, and not demo-verified in this audit).
- Testcontainers integration (none exists; `34-` records this honestly).

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| S-01 | CONFIRMED | Medium | Root POM does not aggregate the starter/demo modules, so "the build" and "the integrations" are different things; CI builds only the core (see 44). |
| S-02 | ACCEPTABLE | Low | Out-of-scope list above must be reflected in README claims (done in this audit; see 40). |

## Improvement Plan
Add an aggregator/flatten POM strategy in a later minor release so `mvn verify` at root covers starters; publish a SUPPORTED-MATRIX page.

## Acceptance Criteria
README scope claims map 1:1 to items in the In-Scope table with a link to evidence; every out-of-scope item is absent from README marketing.

## Final Status
**CONDITIONAL PASS** (scope is honest after README corrections; module aggregation deferred post-release).
