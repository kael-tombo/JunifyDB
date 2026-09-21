# 60 — Public Release Checklist

## Scope
Objective, checkboxed verification for the public GitHub release of 1.0.0.

## Checklist (state at audit close)
### Code & Tests
- [x] Clean-clone `mvn clean test` green — **669 baseline / 677 post-fix, 0 failures** (`.freebuff/baseline-build.log`, `.freebuff/postfix-full-suite2.log`)
- [x] Critical durability/conflict defects fixed **with pre-fix failing evidence** (`.freebuff/prefix-failures.log`)
- [x] New regression suite `ReleaseAuditRegressionTest` (8 tests) in CI path
- [x] No test asserting the buggy behavior remains (updated where semantics corrected)
- [ ] All 8 demos re-run on the final build (P0-2) ← **do before tag**
- [ ] CHANGELOG amended with audit fixes (P0-3) ← **do before tag**
- [ ] CI Docker job removed/fixed (P0-4) ← **do before tag**

### Documentation & Claims
- [x] README claims verified/bounded/removed (43 verification log)
- [x] Vision-doc contradiction annotated (02)
- [x] Honest engine/durability table (16)
- [ ] ROADMAP refresh (R-23)
- [ ] demo/README prerequisites (P1-6)
- [x] LICENSE/CONTRIBUTING/SECURITY present and consistent

### Security
- [x] Bind-host default 127.0.0.1 verified (22)
- [x] Auth enforcement tests green (22)
- [x] No secrets in repo (54)
- [ ] CVE scan job (P2-4)

### Release Mechanics
- [x] Repo litter removed ($null/server.*)
- [ ] `mvnw` shell script (P1-3)
- [ ] GitHub Release draft: tag `v1.0.0`, attach shaded jar, link audit docs
- [x] Recommended version: **1.0.0** (47-VER-02)
- [ ] Maven Central: blocked until 46 items (P2-1) — GitHub-first release is valid without it

### Console
- [x] 13 panels validated (58 matrix)
- [x] Zero browser console errors (37)
- [ ] Playwright flow suite (P1-5)
- [ ] README screenshots (39-WS-03)

## Final Status
**CONDITIONAL PASS** — release may proceed when the five unchecked pre-tag boxes are closed.
