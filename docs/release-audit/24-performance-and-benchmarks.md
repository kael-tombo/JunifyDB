# 24 — Performance & Benchmarks

## Scope
Benchmark reproducibility, harness quality, and honesty of published numbers.

## Expected Behavior
No performance claim without a reproducible harness; README presents numbers as indicative, not certified.

## Current Implementation
- JMH harness wired behind the `benchmark` profile (`src/benchmarks/java`, jmh 1.37, shaded `benchmarks.jar` with `org.openjdk.jmh.Main`).
- Demo stress suite (`demo/load-and-stress-demo`) produces the throughput/latency table reproduced in the README.
- No JMH results were executed in this audit environment (time-constrained); the README table is sourced from the demo harness output and is now labeled indicative.

## Validation Performed
Baseline + post-fix full suites (677 tests) complete in the same envelope as baseline — no performance regression introduced by the fixes (conflict check adds a map lookup per commit; WAL replay only runs at startup). Qualitative check only; formal before/after microbenchmarks deferred.

## Evidence
- Suite durations comparable across baseline and post-fix logs.
- Harness exists: `pom.xml` benchmark profile; `BenchmarkRunner` in main sources.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| P-01 | CONFIRMED (fixed) | High | README presented demo-harness numbers as "Measured — Not Estimated" with no environment/reproducibility metadata. Reworded to "Indicative — Re-Run It Yourself" with the source harness named. |
| P-02 | PARTIALLY VERIFIED | Medium | JMH suite exists but was not executed for this audit; no certified numbers are claimed anywhere. |

## Improvement Plan
CI job running a short JMH smoke profile and publishing results as artifacts; environment metadata template for benchmark reports.

## Acceptance Criteria
README numbers labeled indicative with repro source (met); JMH profile builds (verified via profile existence + shade config; full JMH run deferred).

## Final Status
**CONDITIONAL PASS**
