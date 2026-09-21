package org.junify.db;

import org.junify.db.config.ConsoleConfig;
import org.junify.db.config.JunifyDBConfig;
import org.junify.db.config.SecurityConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression tests for R-28: mutations executed through the SQL console path
 * ({@code POST /api/sql}) must appear in the audit trail exactly like
 * REST-path CRUD; pure reads must not create audit events.
 *
 * <p>Fails before the fix: SQL Studio writes left no audit events because
 * {@code SqlHandler} never called the audit hook.
 */
public class SqlAuditTrailTest {

    private JunifyDB db;
    private int port;

    private static final String API_KEY = "test-secret-api-key-r28";

    @BeforeEach
    void setUp() throws Exception {
        db = JunifyDB.embed()
                .storageEngine(JunifyDBConfig.StorageEngineType.IN_MEMORY)
                .console(ConsoleConfig.builder()
                        .enabled(true)
                        .port(0) // ephemeral port
                        .contextPath("/")
                        .build())
                .security(SecurityConfig.builder()
                        .authEnabled(true)
                        .apiKey(API_KEY)
                        .adminUsername("admin")
                        .adminPassword("admin-secret-pass")
                        .build())
                .build();

        port = db.consolePort();
        assertTrue(port > 0, "Console server should be bound to an active port");
    }

    @AfterEach
    void tearDown() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    private record HttpResult(int code, String body) {}

    private HttpResult send(String method, String path, String body) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:" + port + path).openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("X-API-Key", API_KEY); // machine-to-machine auth bypasses CSRF
        if (body != null) {
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            try (var os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }
        int code = conn.getResponseCode();
        var stream = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
        String resp = "";
        if (stream != null) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            resp = sb.toString();
        }
        return new HttpResult(code, resp);
    }

    private HttpResult sql(String statement) throws Exception {
        return send("POST", "/api/sql", "{\"query\":\"" + statement + "\"}");
    }

    @Test
    @DisplayName("R-28: INSERT via /api/sql is audited (fails before the fix)")
    void sqlInsertIsAudited() throws Exception {
        HttpResult exec = sql("INSERT INTO audit_products (id, name) VALUES ('a1', 'Widget')");
        assertEquals(200, exec.code(), "SQL execution should succeed: " + exec.body());

        HttpResult events = send("GET", "/api/audit/logs?operation=INSERT", null);
        assertEquals(200, events.code());
        assertTrue(events.body().contains("audit_products"),
                "SQL-path INSERT must appear in the audit trail; audit response: " + events.body());
    }

    @Test
    @DisplayName("R-28: UPDATE and DELETE via /api/sql are audited")
    void sqlUpdateAndDeleteAreAudited() throws Exception {
        sql("INSERT INTO audit_products (id, name) VALUES ('c1', 'Cog')");

        HttpResult upd = sql("UPDATE audit_products SET name = 'Sprocket' WHERE id = 'c1'");
        assertEquals(200, upd.code(), "UPDATE should succeed: " + upd.body());
        HttpResult updEvents = send("GET", "/api/audit/logs?operation=UPDATE", null);
        assertTrue(updEvents.body().contains("audit_products"),
                "SQL-path UPDATE must be audited; audit response: " + updEvents.body());

        HttpResult del = sql("DELETE FROM audit_products WHERE id = 'c1'");
        assertEquals(200, del.code(), "DELETE should succeed: " + del.body());
        HttpResult delEvents = send("GET", "/api/audit/logs?operation=DELETE", null);
        assertTrue(delEvents.body().contains("audit_products"),
                "SQL-path DELETE must be audited; audit response: " + delEvents.body());
    }

    @Test
    @DisplayName("R-28: reads via /api/sql are NOT audited")
    void sqlReadsAreNotAudited() throws Exception {
        sql("INSERT INTO audit_products (id, name) VALUES ('b1', 'Gadget')");
        sql("SELECT * FROM audit_products");

        HttpResult events = send("GET", "/api/audit/logs", null);
        assertEquals(200, events.code());
        assertFalse(events.body().contains("\"SELECT\""),
                "Reads must not create audit events; audit response: " + events.body());
    }
}
