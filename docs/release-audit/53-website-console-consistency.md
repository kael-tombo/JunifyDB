# Website ↔ Console Consistency Audit

## Scope
Side-by-side comparison of the deployed website
(https://kael-tombo.github.io/JunifyDB/, fetched live 2026-09-21) and the
embedded Console, across branding, tokens, states, and **truth of claims**.

## Findings

| ID | Area | Finding | Status | Severity | Fix |
|---|---|---|---|---|---|
| 53-WC-01 | Logo/mascot | Console favicon+logo were indigo/violet; site is Volt bolt-on-amber | **FIXED** (52-BR-03) | High | Canonical SVGs now shared |
| 53-WC-02 | Accent color | Console sky-blue `#38bdf8`/`#0284c7` vs site amber | **FIXED** (52-BR-01/02) | High | Amber family with AA/AAA contrast |
| 53-WC-03 | Favicon | Console favicon was an indigo DB-cylinder, not Volt | **FIXED** (52-BR-03) | Medium | Volt bolt favicon |
| 53-WC-04 | **Claims** | **Live site says "NoSQL + ANSI SQL"** — the engine is a built-in dialect, not ANSI (doc 08) | **FIXED LIVE** (2026-09-21): gh-pages updated (`bbd87ca`), Pages rebuilt; live fetch shows old claims 0×, corrected 11× | **High** | gh-pages branch is the deployment mechanism (not Actions) |
| 53-WC-05 | **Claims** | **Live site claims "<15 ms cold start", "85,000+/124,000 ops/s", "Measured"** — only the local stress demo's indicative figures are reproducible (doc 43) | **FIXED LIVE** (2026-09-21, `bbd87ca`) | **High** | Figures marked indicative, anchored to reproducible harness |
| 53-WC-06 | **Claims** | **Live site claims "tamper-evident audit logs"** — audit is a memory ring + JSONL copy (docs 22, R-24) | **FIXED LIVE** (2026-09-21, `bbd87ca`) | **High** | Honest wording live |
| 53-WC-07 | **Coordinates** | **Live site publishes `org.junify:junify-db`** — real coordinates are `org.junify.db:junify-db-core` (pom.xml) | **FIXED LIVE** (2026-09-21, `bbd87ca`): snippet now resolvable | **Critical** | Live copy-paste install works |
| 53-WC-08 | Deployment | Site deploys from `kael-tombo.github.io/JunifyDB` while pushes go to `armand-ratombotiana/JunifyDB` | **MITIGATED** (2026-09-21): `pages.yml` is now **manual-only** (`workflow_dispatch`), so the structurally-failing Actions deploy no longer runs on push — no more permanent red X in the Actions tab. To adopt the Actions model, switch the repo's Pages source to "GitHub Actions", then dispatch the workflow. Branch deploy (gh-pages) remains the live pipeline: main `docs/index.html` → gh-pages publish (procedure in this file) | Medium | Optional owner action: switch Pages source to Actions and re-enable the workflow |
| 53-WC-09 | Terminology | Site "Redis structures" vs docs "KV list/set/hash" | PASS (acceptable synonym) | Low | none |
| 53-WC-10 | Canonical logo | Interim Volt bolt replaced by the owner-supplied navy/amber mark on **all three surfaces** (site nav/hero/footer/favicon, console header/login/favicon, README banner) — same PNG files, single source in `docs/assets/` | **FIXED** (2026-09-21, gh-pages `6ec890f`) | High | Verified by live screenshots of console and site |
| 53-WC-11 | Final mark, 150% sizing | Owner-supplied free-form logo applied to site (hero/nav/footer/favicon) and console (header chip, login, favicon) at 150% of prior sizes with UI retune (header heights, aura, mobile scaling, rail mode) | **FIXED** (2026-09-21, gh-pages `a7455a0`) | High | Live screenshots of both surfaces |
| 53-WC-10 | States | Console empty/error/loading states verified in browser (docs 37/38); site is static marketing | PASS | Low | none |

## Deployment diagnostics (2026-09-21, post-fix verification)

- Live URL fetch after the corrected source landed on main: **old claims still served** (`ANSI SQL` ×10, `15ms` ×5, `org.junify:` ×1, `tamper-evident` ×1) — cache-busting confirms it is not CDN staleness.
- `Last-Modified: Sat, 19 Sep 2026 21:47:08 GMT` + nginx-style `ETag "6aaf02dc-15cf4"`: the site is **frozen at the last successful deployment (Sep 19)** — every `Deploy GitHub Pages` run since #4 has failed at the `deploy` job with **no failed steps** (build job green).
- Fix applied in-repo (`810bbec`): `configure-pages` with `enablement: true` — deploy still failed, so the residual cause is repository-level (Pages source/model or workflow-deployment permission), which requires owner access to Settings → Pages / Actions permissions.

**Owner resolution paths (either one):**
1. Settings → Pages → Source: **"Deploy from a branch"** → `main` + `/docs` (simplest; the corrected `docs/index.html` publishes on next push; then remove the `deploy` job from `pages.yml` to avoid model conflict).
2. Keep the Actions model: Settings → Actions → General → Workflow permissions → **Read and write**; re-run the `Deploy GitHub Pages` workflow (it now auto-enables via `enablement: true`).

## Deployment caveat (honest scope)
Fixes 53-WC-04..07 are applied to the **site source in this repo**
(`docs/index.html`) and will reach the live URL only when GitHub Pages
redeploys from `main`. Until then the live site remains misrepresenting —
**this is the one brand/claims item that outlives this commit** and is carried
into the blocker register (55) as *mitigated in source, pending redeploy*.

## Validation Performed
- Live fetch of the deployed site (read_url, 2026-09-21): claim strings quoted verbatim.
- Local `docs/index.html` grep counts: `ANSI SQL` ×10, `15ms` ×5, `tamper-evident` ×1, `org.junify:` ×1 — re-counted after fix.
- Console: live browser pass on the running preview (Overview, Collections, Audit Trail; all API 200).

## Final Assessment
**CONDITIONAL PASS** — console side fully aligned and verified in-browser;
site source corrected, live redeploy pending (owner-side Pages action).
