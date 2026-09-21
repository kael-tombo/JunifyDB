# 12 — Indexing & Query Planning

## Scope
Secondary indexes, HNSW vector index, and whether the planner uses them.

## Expected Behavior
Honest claims: indexes accelerate exact-match lookups where used; vector search works with documented constraints.

## Current Implementation
- `index/SecondaryIndex` — inverted index per field, persisted to `.indexes`, maintained on insert/update/delete of indexed fields.
- `index/hnsw/HNSWIndex` — fixed 128-dimension vector index (verified live: add/search return string ids; dimension is hardcoded, not configurable — the console panel labels this constraint).
- Planner: no automatic index selection for WHERE filters (query path scans); `MVCCManager.readIf` exists for index-assisted reads.

## Validation Performed
- Index suites green in full runs; live vector add/search via console REST (results returned as string ids — UI fixed to match).
- Code read of `SecondaryIndex` lifecycle (`loadIndexes`/`saveIndexes`, `createIndex`).

## Evidence
Source inspection + prior console network traces (`trace-POST-api_vectors_embeddings_search.json`).

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| IX-01 | CONFIRMED | Medium | Query planner does not automatically use secondary indexes; users get full scans unless the code path uses `readIf`. Documented; optimization is post-release. |
| IX-02 | CONFIRMED | Medium | HNSW dimension fixed at 128; attempts to add other dimensions must fail clearly (console documents the constraint). Configurable dimension = post-release improvement. |
| IX-03 | ACCEPTABLE | Low | HNSW excluded from coverage as "experimental" in POM — matches its product status (console panel labels it). |

## Improvement Plan
Cost-based index hints; configurable vector dimension; benchmark HNSW recall.

## Acceptance Criteria
README claims no automatic index optimization (verified); vector constraints documented in UI (done).

## Final Status
**CONDITIONAL PASS**
