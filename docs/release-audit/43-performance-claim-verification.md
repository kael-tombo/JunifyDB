# 43 — Performance Claim Verification

## Scope
Each previously published performance claim, verified or rewritten.

## Verification Log
| Claim (pre-audit) | Verification | Resolution |
|---|---|---|
| "ANSI SQL" (perf-adjacent positioning) | Parser is a subset dialect | README rewritten (08) |
| "< 15 ms startup" | Not measured in audit env; plausible but unverified | Rewritten: "single-digit ms (measured in-process)" → "starts in milliseconds" |
| Throughput table (2,155–54,545 ops/sec) | Sourced from demo stress harness; not re-run here | Relabeled "Indicative — Re-Run It Yourself" with source named (24) |
| "0 violations read-after-write" | Supported by stress suite assertions (green) | Kept, presented as harness output |
| "production-grade" | Unsupported superlative | Removed |
| "B-Tree page store / >RAM datasets" | All engines heap-resident (source-verified) | Table corrected (16-D-03) |
| "WAL with deterministic fsync crash durability" | fsync real (`getFD().sync()`); **recovery was dead pre-fix** | Recovery fixed (15); claim now true |
| "Tamper-evident audit log" | In-memory ring buffer | Reworded (22-SEC-03) |

## Evidence
Each row traces to audit file sections above; README diff.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| PC-01 | CONFIRMED (fixed) | Critical | The durability claim was false pre-fix despite real fsync — the most dangerous kind of claim (safety-critical). Now true with regression proof (15). |
| PC-02 | CONFIRMED | Low | Remaining claims are bounded and reproducible via named harnesses. |

## Improvement Plan
Re-run stress + JMH before tagging; paste environment metadata into README table.

## Acceptance Criteria
No unverifiable perf claim remains (met).

## Final Status
**CONDITIONAL PASS** (was RELEASE BLOCKER as a claims set)
