# 03 — Repository Architecture

## Scope
Top-level repository layout, tracked content hygiene, and navigation for new contributors.

## Expected Behavior
A fresh clone contains only source, build config, docs and scripts needed to build/run; no build artifacts, no OS/IDE litter; README points to the right directories.

## Current Implementation
Root contains: `src/`, `docs/`, `demo/`, `cli/`, `spring-boot-starter/`, `quarkus-extension/`, `micronaut-integration/`, `.github/`, `.mvn/`, `release-audit/` (older self-audit), `release-evidence/` (traces), `README.md`, `CHANGELOG.md`, `CONTRIBUTING.md`, `SECURITY.md`, `LICENSE`, `ROADMAP.md`, `pom.xml`, `mvnw.cmd`, `deep-test.ps1`.

## Validation Performed
`git ls-files` audit; `git status` before/after fixes; removal of tracked litter.

## Evidence
- Baseline `git ls-files` included `$null` (0-byte), `server.err`, `server.out` (stale run logs mentioning "Storage engine: H2" — misleading).
- `.gitignore` lacked `.freebuff/` (agent workspace) and audit worktree dirs.
- Fixed in this audit: files removed; `.gitignore` extended.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| RA-01 | CONFIRMED (fixed) | Low | `$null`, `server.err`, `server.out` tracked in git; removed. |
| RA-02 | CONFIRMED | Low | Two parallel audit/evidence trees (`release-audit/`, `release-evidence/`) predate `docs/release-audit/`; risk of stale, contradictory self-assessment. |
| RA-03 | CONFIRMED | Low | `.classpath`, `.project`, `.settings/` (Eclipse metadata) tracked — usually per-developer. |

## Improvement Plan
Archive or merge the old `release-audit/` + `release-evidence/` under `docs/history/`; add `.classpath/.project/.settings/` to `.gitignore` in a follow-up commit.

## Acceptance Criteria
`git ls-files` contains no `$null`, `server.*`; `.gitignore` covers agent/worktree dirs (verified).

## Final Status
**CONDITIONAL PASS** (litter removed; legacy tree consolidation deferred).
