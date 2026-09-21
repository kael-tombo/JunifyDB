package org.junify.db.core.exception;

/**
 * Base class for all JunifyDB runtime exceptions (audit R-16 / 21-E-01).
 *
 * <p>Unchecked by design: embedded-database failures are almost never
 * recoverable at the call site, and forcing checked handling pushed every
 * caller into wrapping the exception in a bare {@link RuntimeException},
 * which destroyed type information. Catching {@code JunifyDBException} now
 * catches every engine-level failure JunifyDB raises deliberately.
 *
 * <p>Hierarchy:
 * <pre>
 * JunifyDBException
 * ├── SerializationException   (JSON encode/decode, entity mapping)
 * └── StorageException        (engine init, flush, persistence I/O)
 * </pre>
 */
public class JunifyDBException extends RuntimeException {

    public JunifyDBException(String message) {
        super(message);
    }

    public JunifyDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
