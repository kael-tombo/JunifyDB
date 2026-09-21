# 46 — Maven Central Readiness

## Scope
Publishability to Maven Central per current requirements.

## Current Implementation vs Central Checklist
| Requirement | Status |
|---|---|
| Valid GAV coordinates (`org.junify.db:junify-db-core:1.0.0`) | ✅ |
| Project name/description/url | ✅ |
| Licenses (Apache-2.0) | ✅ |
| Developer info | ✅ |
| SCM metadata | ✅ |
| Source jar | ❌ plugin missing |
| Javadoc jar | ❌ plugin missing |
| GPG signing | ❌ plugin/config missing |
| No snapshot deps | ✅ |
| No system/local-path deps | ✅ |
| Reproducible build config | ⚠️ not configured (no `-Dproject.build.outputTimestamp`) |
| Published coordinates match README | ✅ (README references building from source; no install-snippet yet — add one at publication) |

## Validation Performed
POM audit in this session; no staging/dry-run performed (no OSSRH credentials/network in this environment) — per rule: **no Central-readiness claim is made without executing the process**.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| MC-01 | CONFIRMED | High | Missing source/javadoc/GPG plugins + signing config blocks Central publication today. These are mechanical additions (0.5–1 day). |
| MC-02 | NOT VERIFIED | High | Actual staging + resolution-by-consumer not executed — must be done before any "available on Maven Central" claim. |
| MC-03 | CONFIRMED | Low | `createDependencyReducedPom=false` on shade: fine while publishing only the plain jar; revisit if the shaded jar is ever published. |

## Improvement Plan
Add maven-source-plugin, maven-javadoc-plugin, maven-gpg-plugin under a `release` profile; set `project.build.outputTimestamp` for reproducibility; run `mvn deploy -Prelease` to OSSRH staging; verify consumer resolution from Central.

## Acceptance Criteria
Dry-run staging succeeds; consumer project resolves all artifacts; then and only then publish.

## Final Status
**FAIL** (as "Central-ready today") — **POST-RELEASE PATH DOCUMENTED** (GitHub-first release is viable without Central; see 61).
