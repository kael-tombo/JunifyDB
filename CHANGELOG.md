# Changelog

All notable changes to **JunifyDB (JNoSQL-EMBED)** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-09-09

### Added
- **Multi-Model NoSQL Engine**:
  - Document Collections supporting nested JSON, schema-free storage, and TTL.
  - Secondary Inverted Indexes supporting fast exact-match lookup.
  - Aggregation Pipelines supporting `sum`, `avg`, `min`, `max`, and `groupBy`.
  - Redis-compatible Key-Value data structures: Simple KV, Hash Bucket, List Bucket, and Set Bucket.
  - Wide-Column Family store supporting column-level TTL, range queries, and pagination.
- **Pluggable Storage Engine Architecture**:
  - `IN_MEMORY`: Ultra-fast `ConcurrentHashMap` with CRC32 removal for maximum throughput.
  - `FILE`: Human-readable JSON disk persistence with background flush.
  - `B_TREE`: Clustered binary index with fast range scans.
  - `LSM_TREE`: Log-Structured Merge Tree with MemTable, Bloom Filter, immutable SSTables, and compaction.
- **ACID Transaction Manager**:
  - MVCC multi-version concurrency control with snapshot isolation.
  - Atomic commit, rollback, and conflict detection.
  - Write-Ahead Log (WAL) with deterministic fsync crash durability.
- **JVM Framework Integrations**:
  - `junify-db-spring-boot-starter`: Spring Boot auto-configuration and `JunifyDBTemplate`.
  - `junify-db-quarkus-extension`: Quarkus SmallRye config and CDI `@DefaultBean` producers.
  - `junifydb-micronaut-integration`: Micronaut `@Factory` and Serde reflection-free serialization.
  - Eclipse Vert.x: Non-blocking reactive verticle examples with event loop protection.
- **Developer Experience & Administration**:
  - Built-in HTTP Admin Server and Developer Console (`JunifyDBServer`).
  - API Key authentication and audit logging.
  - Production Demonstration Suite with canonical E-Commerce domain (`demo/`).
  - Comprehensive documentation covering Vision, Architecture, Features, Testing, and Runbooks.

### Fixed
- Fixed Windows file-locking defect in `WriteAheadLog` by ensuring `logFileOutputStream` closes cleanly on database shutdown.
- **Release-audit fixes (September 2026):**
  - MVCC write-write conflict detection was unreachable; commits now validate the write set against the transaction's snapshot timestamp (first-writer-wins). `MVCCManager.commit(txId, commitTs, readTimestamp)` added; the 2-arg overload is retained.
  - `FileEngine` now replays its write-ahead log on startup (entries newer than the last checkpoint), so writes not yet flushed to JSON snapshots are no longer lost on an unclean shutdown.
  - `LSMTreeEngine` now replays its WAL even when SSTables exist, adds recovered keys to the bloom filter, and initializes its WAL writer after recovery; compaction and SSTable ordering are consistently oldest-to-newest with newest-first reads.
  - Collections persisted by a previous run are re-exposed after restart via the new `StorageEngine.collectionNames()` SPI (`JunifyDB.getCollectionNames()` now reflects on-disk state for the FILE engine).
- Resolved LSM-Tree bloom filter cold-restart bug by populating filter directly from loaded SSTables.
- Fixed B-Tree engine shutdown check-open order to guarantee dirty keys flush before marking engine closed.
