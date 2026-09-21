# 40 — README & Documentation Audit

## Scope
README.md and the docs/ tree as the onboarding surface.

## Expected Behavior
Every claim executable by a new developer; docs internally consistent; limitations disclosed.

## Current Implementation (post-corrections)
- README: positioning, philosophy, quick-start, engines table (with honest B-Tree caveat), SQL dialect boundary, annotation support, console description (honest audit-trail wording), demo suite, use cases (bounded startup claim), corrected comparison table, indicative performance table.
- docs/: vision (annotated), features (BROKEN/PARTIAL/MISSING trackers — unusually honest), architecture, admin-console validation, browser evidence, runbooks.
- CHANGELOG 1.0.0 dated 2026-09-09; CONTRIBUTING; SECURITY present.

## Validation Performed
Read + targeted greps; corrections applied in this audit (listed in 02/22/24/39). Quick-start command verified: `mvn -q -DskipTests package` produces the shaded jar that runs the console (used repeatedly here).

## Evidence
README diff; jar runs in this audit.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| DOC-01 | CONFIRMED (fixed) | High | Overclaims removed: "ANSI SQL", "tamper-evident", "<15 ms", "Measured — Not Estimated", "production-grade demo suite". |
| DOC-02 | CONFIRMED | Medium | `demo/` prerequisites (install core first) undocumented (see 35). |
| DOC-03 | CONFIRMED | Low | `docs/vision/CORRECTED-PROJECT-VISION.md` retains superseded SQL scope statement — annotated, archive post-release (02). |
| DOC-04 | CONFIRMED | Low | No Javadoc publication configured (see 42/46). |

## Improvement Plan
demo/README commands; docs sitemap; Javadoc publication via CI.

## Acceptance Criteria
README claims map to audit files 05/06/15/16/22/24 (met); quick-start works (met).

## Final Status
**CONDITIONAL PASS**
