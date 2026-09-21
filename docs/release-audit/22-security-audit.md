# 22 — Security Audit

## Scope
Console/server auth, injection, deserialization, headers, secrets, CORS/CSRF, DoS surface, dependency posture.

## Expected Behavior
No default-insecure posture for non-localhost; no injection through API paths; safe Jackson config; honest documentation of the embedded-admin threat model.

## Current Implementation
- `JunifyDBServer`: API-key auth (optional, `setApiKey`); `SecureSessionManager`; security headers incl. CSP, X-Frame-Options DENY, nosniff, HSTS when TLS; TLS supported via keystore config. `SecurityConfig`/`ConsoleConfig` gate host exposure: `localhostOnly` forces host to `127.0.0.1` even if `0.0.0.0` is requested (`ConsoleConfig` line 71).
- Bind default: `ConsoleConfig.DEFAULT_HOST = "127.0.0.1"` — **console does NOT bind all interfaces by default** (verified this audit; earlier assumption corrected).
- Auth disabled → prominent startup WARN: "Authentication DISABLEED — all API endpoints are publicly accessible" (observed in test logs).
- Jackson: no default typing enabled (checked `JsonSerde`).
- Queries: SQL parser builds internal ops (no string concat into storage keys); REST handlers route through the same engine paths (no raw eval).

## Validation Performed
`SecurityEnforcementTest` (5 tests) green in all runs; auth enforcement verified by tests (401 paths); header set inspected in source; bind-host default verified in source.

## Evidence
- `ConsoleConfig.java` line 26 + 71; `JunifyDBServer.addSecurityHeaders` source read.
- WARN lines visible in `.freebuff/postfix-full-suite2.log`.

## Findings
| ID | Status | Severity | Description |
|---|---|---|---|
| SEC-01 | CONFIRMED | Medium | Auth is opt-in; enabled-by-default would break zero-config DX, so this is a documented trade-off — but README/docs must tell users to set an API key when binding beyond localhost (localhostOnly guard covers the common case). Acceptance: README security note added (done in this audit via console section wording and 40). |
| SEC-02 | CONFIRMED | Low | CSP includes `unsafe-inline` for scripts/styles (single-page console needs it); acceptable for a localhost admin tool, documented. |
| SEC-03 | CONFIRMED | Low | Audit log is in-memory ring buffer (not tamper-evident) — claim corrected in README. |
| SEC-04 | CONFIRMED | Low | No CSRF token: console is localhost single-user; REST API is key-authenticated. Documented threat model; CSRF hardening post-release if multi-user ever supported. |
| SEC-05 | NOT VERIFIED | Low | Third-party dependency CVE scan not run in this environment (no network-scanner available); Jackson 2.17.0 and JUnit 5.10.2 are current-generation versions; recorded for CI follow-up (OWASP dependency-check). |

## Improvement Plan
Add OWASP dependency-check to CI; optional `--require-auth` strict mode; per-origin CORS allowlist config (current CORS echoes origin with credentials when configured — restrict by default post-1.0).

## Acceptance Criteria
Security suite green; bind-host default safe (verified); README not overclaiming (met).

## Final Status
**CONDITIONAL PASS**
