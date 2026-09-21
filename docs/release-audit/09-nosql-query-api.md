# 09 — NoSQL Query API

## Scope
Document query model: filters, projections, sorting, pagination, aggregations, fluent `EntityQuery`, reactive API.

## Expected Behavior
Queries behave as documented in `docs/features/` and README examples; aggregations (`sum`, `avg`, `min`, `max`, `groupBy`) correct.

## Current Implementation
`DocumentCollection.find(Query)`/`findOne`, `Query` criteria model, aggregation pipelines (`AggregationPipelineTest`), `api/EntityQuery` fluent builder, `api/reactive/ReactiveJNoSQL`.

## Validation Performed
Baseline + post-fix suites green (AdvancedQueryTest, AggregationPipelineTest, DocumentCollectionTest among 677). Live console query flows in prior sessions.

## Evidence
Test classes present and passing in `.freebuff/baseline-build.log` and `.freebuff/postfix-full-suite2.log`.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| QA-01 | CONFIRMED (fixed) | Critical | Query results were subject to the durability defects (unflushed data invisible after crash). WAL replay fixes protect query correctness after restart. |
| QA-02 | CONFIRMED | Medium | Index-assisted lookup exists (`SecondaryIndex`, `readIf`) but the query planner does not automatically choose indexes for filters — queries scan. Documented as limitation; see 12. |

## Improvement Plan
Publish query-capability matrix; planner-assisted index selection post-1.0.

## Acceptance Criteria
Query suites green; README makes no automatic-index claim (verified).

## Final Status
**CONDITIONAL PASS**
