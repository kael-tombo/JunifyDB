# 45 — Dependency & Supply-Chain Audit

## Scope
Direct/transitive dependencies, plugin versions, supply-chain posture.

## Current Implementation (runtime deps of core)
| Dependency | Version | Scope | Notes |
|---|---|---|---|
| jackson-databind | 2.17.0 | compile | current generation |
| jackson-datatype-jsr310 | 2.17.0 | compile | |
| slf4j-api | 2.0.12 | compile | |
| slf4j-simple | 2.0.12 | runtime, optional | excluded from shade |
| jakarta.enterprise.cdi-api | 4.0.1 | provided, optional | |
| jnosql-mapping-api-core | 1.1.8 | provided, optional | |
| jakarta.persistence-api | 3.1.0 | provided, optional | |
| hibernate-core | 6.4.4.Final | provided, optional | annotation support only |
| junit-jupiter | 5.10.2 | test | |

No snapshot versions; no private/local repository deps; no system-scope paths. `provided+optional` pattern correctly keeps consumer classpaths clean (verified in POM comments and shade exclusions).

## Validation Performed
POM read; artifact tree not fully resolved offline in this environment; CVE scan not run (22-SEC-05). Plugin versions current (compiler 3.13.0, surefire 3.2.5, shade 3.5.2, jacoco 0.8.13).

## Evidence
`pom.xml` as recorded in 00-baseline.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| SC-01 | CONFIRMED | Low | Minimal, current, well-scoped dependency set — a genuine strength. |
| SC-02 | NOT VERIFIED | Medium | No CVE scan executed here; add OWASP dependency-check / `versions-display-plugin-updates` in CI. |

## Improvement Plan
CI supply-chain job (dependency-check, enforcer rules for convergence).

## Acceptance Criteria
No snapshot/system/local-path deps (met); CVE posture verified in CI (scheduled).

## Final Status
**CONDITIONAL PASS**
