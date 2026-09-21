# 39 — Website & Public Presentation

## Scope
Public-facing surfaces: README badges, docs site (pages.yml publishes docs/), ROADMAP, SECURITY.

## Expected Behavior
Site never promises unverified capabilities; badges accurate; security reporting path exists.

## Current Implementation
- `.github/workflows/pages.yml` publishes `docs/` as the site.
- `README.md` corrected in this audit: SQL dialect bounded, perf labeled indicative, tamper-evident claim removed, engine capabilities accurate.
- `SECURITY.md` exists (reporting path); `ROADMAP.md` scopes SQL evolution to 1.2 (now partially delivered by the built-in engine — roadmap needs a refresh to reflect shipped state).
- Vision doc contradiction annotated as superseded (see 02).

## Validation Performed
Grep for overclaim patterns post-fix: "ANSI" reduced to dialect note + one comment line; "tamper" removed from README; "production-grade" removed from demo claim; perf header rewritten.

## Evidence
README diff in this audit; `grep` outputs recorded in session.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| WS-01 | CONFIRMED (fixed) | High | Public claims previously contradicted implementation (SQL story, tamper-evident, measured-looking benchmarks). Corrected. |
| WS-02 | CONFIRMED | Medium | ROADMAP predates the SQL engine's existence — refresh needed so the public roadmap doesn't "plan" something already shipped. |
| WS-03 | ACCEPTABLE | Low | No screenshots embedded in README; the redesigned console would benefit from 2–3 (post-release, requires working screenshot tooling). |

## Improvement Plan
Refresh ROADMAP; add screenshots; publish the audit's honest capability matrix on the site.

## Acceptance Criteria
No unverified capability promised on public surfaces (met after corrections).

## Final Status
**CONDITIONAL PASS**
