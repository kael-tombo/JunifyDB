# 06 — NoSQL Engine Audit

## Scope
Document, KV, List, Set, Hash and Column-Family models: CRUD, queries, TTL, persistence, error behavior.

## Expected Behavior
Redis-style semantics for KV structures, document CRUD with secondary indexes, column-family range/pagination/TTL — all per README and demo evidence.

## Current Implementation
`nosql/document/DocumentCollection` (insert/find/findOne/update/upsert/deleteById/deleteAll/bulkDelete, secondary indexes, TTL via `expiresAt`), `nosql/kv/*` buckets, `column/ColumnFamily`.

## Validation Performed
- Full baseline + post-fix suites green (KV/List/Set/Hash/Column/Document/Aggregation/TTL tests among 677).
- Live console round-trips (prior session): KV put/get, list rpush/range, set sadd/smembers (`added: 2`), hash hset/hgetall, SQL↔document cross-model reads, vector add/search (128-dim, string ids).
- Console tests `ConsoleFeatureValidationTest`, `SecurityEnforcementTest` green.

## Evidence
- Console/network traces under `docs/browser-testing/evidence/network/`.
- `SetBucket`/`HashBucket`/`ListBucket` write through `engine.put(name, key, serialized)` — same engines, hence same durability characteristics as documents.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| N-01 | CONFIRMED (fixed) | High | All NoSQL models inherit engine durability; File/LSM WAL replay fixes (see 15) directly protect them. Pre-fix, every model could silently lose unflushed writes. |
| N-02 | CONFIRMED | Medium | TTL expiry is lazy (`isExpired()` checks on read/cleanup) — expired docs consume space until touched; documented behavior, acceptable. |
| N-03 | PARTIALLY VERIFIED | Low | Redis wire-protocol compatibility is NOT claimed by the project and none exists — noted to prevent assumption. |

## Improvement Plan
Add TTL jitter/active-expiry sweep as post-release improvement; document per-model semantics table (exists partially in docs/features/).

## Acceptance Criteria
NoSQL suites green post-fix; console round-trips recorded; README claims bounded (done).

## Final Status
**PASS** (as an embedded multi-model store, with durability now genuinely backed by WAL replay)
