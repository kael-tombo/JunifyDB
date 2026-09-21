# 58 — UI Validation Matrix

## Scope
Per-panel validation status of the redesigned console.

| Panel | Valid Input | Invalid Input | Empty State | Error State | Persistence | Evidence |
|---|---|---|---|---|---|---|
| Overview | ✅ live metrics | n/a | ✅ | ✅ toasts | n/a | snapshot + evaluation |
| SQL Studio | ✅ SELECT/INSERT/UPDATE/DELETE | ✅ parser errors surfaced | ✅ | ✅ timing+error | ✅ history (localStorage) | live round-trips |
| Collections | ✅ CRUD, detail | ✅ | ✅ | ✅ | ✅ on-disk JSON | live + restart |
| Key-Value | ✅ put/get/delete/TTL | ✅ | ✅ | ✅ | ✅ WAL | live |
| Lists / Sets / Hashes | ✅ round-trips | ✅ (contract fixed) | ✅ | ✅ | ✅ | live (`added: 2`, hgetall) |
| Column Family | ✅ write/scan | ✅ | ✅ | ✅ | ✅ | live |
| Vectors | ✅ 128-dim add/search | ✅ non-128 labeled | ✅ | ✅ | ⚠️ in-memory index | live (constraint labeled) |
| Schema | ✅ register/list | ✅ | ✅ | ✅ | ✅ | live POST round-trip |
| Transactions | ✅ begin/commit/rollback | ✅ | ✅ | ✅ | n/a | live 1→0 active |
| Indexes | ✅ create/list | ✅ | ✅ | ✅ | ✅ `.indexes` | live |
| Backup | ✅ create/restore | ✅ | ✅ | ✅ | ✅ | live |
| CDC | ✅ connector status | n/a | ✅ | ✅ | n/a | live (status-only, honest) |
| Audit | ✅ recent ops | n/a | ✅ | ✅ | ⚠️ memory ring | live |
| Server | ✅ JVM/health stats | n/a | ✅ | ✅ | n/a | live sparkline |

Cross-cutting: keyboard nav (1–9,0) ✅; theme persistence ✅; hash routing ✅; loading states ✅ (per-request spinners); browser console errors **zero** at final state.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| UM-01 | PARTIALLY VERIFIED | Low | Screenshot compositing unavailable in audit environment — flows verified via snapshots/DOM/network; visual-regression tooling post-release (37-UI-02). |
| UM-02 | CONFIRMED | Low | Responsive/mobile widths not exercised; desktop-only verification. |

## Final Status
**CONDITIONAL PASS**
