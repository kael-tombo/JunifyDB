# 19 — TTL & Expiration

## Scope
TTL on documents and KV entries; expiration semantics and cleanup.

## Expected Behavior
TTL'd entries become invisible after expiry; space eventually reclaimed; TTL across restarts honored.

## Current Implementation
- Documents: `insert(doc, ttlSeconds)` sets `expiresAt`; `isExpired()` checks on read; `cleanupExpired()` sweeps.
- KV: expirations serialized under `meta_store` collection (`kv_expirations_<bucket>`), so they persist across restarts for durable engines.

## Validation Performed
TTL-related tests green in full suites; code read of expiry checks (`isExpired`, `cleanupExpired`, KV expiration storage).

## Evidence
`DocumentCollection` lines 440–448 (`cleanupExpired`); `KeyValueBucket` lines 43–77.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| TTL-01 | CONFIRMED | Medium | Expiry is lazy: expired entries remain on disk until read or swept; no background reaper. Acceptable for 1.0; documented. |
| TTL-02 | ACCEPTABLE | Low | TTL timestamps wall-clock based; clock skew can shorten/lengthen TTLs (standard caveat). |

## Improvement Plan
Scheduled active-expiry sweep; TTL metrics.

## Acceptance Criteria
TTL tests green; behavior documented (met).

## Final Status
**PASS**
