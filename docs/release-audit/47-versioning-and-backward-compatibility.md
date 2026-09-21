# 47 — Versioning & Backward Compatibility

## Scope
Version strategy, semver commitment, compatibility of the audit's changes.

## Current Implementation
- Version `1.0.0` declared in POM + CHANGELOG dated 2026-09-09 (Keep-a-Changelog format, semver reference).
- This audit's compatibility surface: `StorageEngine.collectionNames()` default method (additive); `MVCCManager.commit` 3-arg added with 2-arg delegating overload (additive); behavior fixes (conflict detection now fires; WAL replay) are **correctness changes** — programs relying on last-writer-wins or on silent WAL loss would observe different (correct) behavior; that is the point of a 1.0 fix and is documented in CHANGELOG-worthy detail here.
- No deprecation policy documented.

## Validation Performed
Direct pre-existing callers compile unchanged (`DeepTransactionTest` 4 call sites; full suite green).

## Evidence
Suite logs; diff scope limited to 5 production files.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| VER-01 | CONFIRMED | Low | 1.0.0 as the baseline is appropriate: nothing external exists to break; correctness fixes land pre-publication where they belong. |
| VER-02 | CONFIRMED | Low | Recommended pre-publication version: keep **1.0.0** with CHANGELOG amended for the audit fixes (or 1.0.1 if the maintainer prefers the 2026-09-09 entry untouched). |

## Improvement Plan
Document semver + deprecation policy in CONTRIBUTING; amend CHANGELOG "Fixed" section for R-01..R-04.

## Acceptance Criteria
Suite green with additive-only API changes (met).

## Final Status
**PASS**
