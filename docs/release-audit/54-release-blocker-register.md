# 54 — Release-Blocker Register

## Scope
Application of the automatic-blocker policy to this audit's findings.

## Blocker Evaluation
| Policy Blocker | Findings | Disposition |
|---|---|---|
| Data loss | R-02/R-03 (silent WAL loss) | **MITIGATED — fixed** with regression proof; durability contract now real (15) |
| Data corruption | R-05/R-06 (LSM compaction/ordering/key extraction) | **MITIGATED — fixed**; engine suite green (16) |
| Unrecoverable persistence failure | same as above | **MITIGATED — fixed** |
| Broken transaction semantics | R-01 (conflicts never detected) | **MITIGATED — fixed** with regression proof (13) |
| Security vulnerability / default insecure access | Console binds 127.0.0.1 by default; auth opt-in with loud warning; localhost guard forces loopback | **MITIGATED — acceptable** for embedded admin tool; documented threat model (22) |
| Publicly exposed secrets | None found (no credentials in repo; run docs record procedures only) | **CLEAR** |
| Misleading compatibility claims | R-07 (README overclaims; vision contradiction) | **MITIGATED — fixed** (02/40/43) |
| Broken clean installation | Baseline build green from clean state (env-lock artifact documented) | **CLEAR** |
| Broken Maven publication | R-13: not Central-ready (plugins missing) | **BLOCKS CENTRAL ONLY** — not GitHub-first release; disposition: GitHub-first (61), Central after plugins + staging run |
| Demos not working as documented | R-22: prerequisite undocumented; not all re-executed here | **MITIGATED — acceptable** with documented follow-through in 60 (run-all before tag) |
| Documented feature entirely non-functional | CDC (documented as status-only, not claimed as change stream); B-Tree "page store" claim removed | **MITIGATED — fixed/corrected** |
| Console actions silently losing/corrupting data | Delete confirmations verified; durability fixes close the loss paths | **MITIGATED — fixed** |
| Tests falsely reporting success | `LSMTreeEngineTest.testPersistence` previously passed for the wrong reason (malformed keys); now asserts correct behavior | **MITIGATED — fixed** (16-R-06) |
| Build not reproducible | Clean-clone build green (2× in audit) | **CLEAR** |
| Framework integration advertised but non-functional | Starters: functional by prior demo evidence + unit tests, not CI-verified | **CONDITIONAL — README badges link to demos; CI closure scheduled (44)** — accepted for GitHub-first release with badge wording tied to demo evidence |

## Open Blockers for Public GitHub Release
**None.**

## Open Blockers for Maven Central Publication
MC-01/MC-02 (source/javadoc/GPG plugins + verified staging) — documented path in 46.

## Final Status
**CONDITIONAL PASS** (GitHub-first GO path clear; Central gated)

## 2026-09-21 final validation round
| ID | Finding | Status | Disposition |
|---|---|---|---|
| RB-20 | Live website (Pages) shows false claims + wrong coordinates; fixed in docs/index.html | MITIGATED in source | BLOCKS live-site claim only until Pages redeploy; product release otherwise GO (doc 62) |
| RB-21 | Pages deployed from kael-tombo account vs armand-ratombotiana repo | OPEN (owner) | Account-level setting; flagged 53-WC-08 |
| RB-22 | Published core jar 6.9 MB exceeded 5 MB requirement (bundled byte-buddy) | **FIXED** | SZ-06: excluded; measured 2.89 MB; CI size gate added |
| RB-23 | Console used off-brand blue/indigo identity vs yellow site | **FIXED** | Amber rebrand, browser-verified (52-BR-01..05) |
