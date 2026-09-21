# 26 — Jakarta NoSQL Compatibility

## Scope
Whether JunifyDB satisfies its stated Jakarta NoSQL behavior — with specification-aware honesty.

## Expected Behavior
Per rule 16: if a feature is intentionally unsupported, document it rather than pretend. The project's own claim (pom description: "Embedded JNoSQL-style NoSQL database") is "JNoSQL-style", not certified compatibility.

## Current Implementation
- Optional `provided` dependency on `org.eclipse.jnosql.mapping:jnosql-mapping-api-core:1.1.8`.
- `adapter/jnosql/EntityMapper`, `JunifyRepository`, `CrudRepository`, `JunifyDBProducer` map `jakarta.nosql.@Entity/@Id/@Column` annotations reflectively; `api/reactive/ReactiveJNoSQL` exists.
- `JunifyRepositoryTest`, `JunifyDBProducer` paths tested in suite.

## Validation Performed
Annotation-mapping tests green; **no Testcontainers/spec-TCK run, no Eclipse JNoSQL driver integration executed** in this audit. Therefore: annotation *dialect* compatibility is verified; framework *specification* compatibility is NOT claimed.

## Evidence
POM dependency block; `EntityMapper` source; test suite logs.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| JN-01 | CONFIRMED | Medium | "JNoSQL-style" is accurate marketing; "compatible with Eclipse JNoSQL" would be false until a real `jnosql-communication-*` driver bridges `JunifyDB` into the JNoSQL `StorageManager` SPI. Not claimed anywhere after this audit's wording check. |
| JN-02 | CONFIRMED (fixed, prior session) | Low | Console Hybrid panel previously crashed (`escapeHtml is not defined`) during JNoSQL-style hybrid demos; UI rewritten. |

## Improvement Plan
Post-1.0: implement a `JunifyDBDocumentManager implements DocumentManager` bridge module + run JNoSQL's integration tests against it before claiming compatibility.

## Acceptance Criteria
No unqualified JNoSQL compatibility claim in README (met — checked).

## Final Status
**CONDITIONAL PASS** (honest boundary documented)
